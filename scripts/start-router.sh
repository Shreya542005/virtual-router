
#!/bin/bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
UPLINK="wlp2s0"
AP="ap0"
RUN_DIR="/run/virtual-router"
LOG_DIR="/var/log/virtual-router"

DHCP_CONF="$ROOT_DIR/config/dhcp/ap-dhcp.conf"
HOSTAPD_CONF="$ROOT_DIR/config/hostapd/hostapd.conf"
FIREWALL_CONF="$ROOT_DIR/config/logging/firewall-logging.v4"

fail() {
    echo "ERROR: $1" >&2
    exit 1
}

[ "$(id -u)" -eq 0 ] || fail "Run with sudo."
[ -f "$HOSTAPD_CONF" ] || fail "Hostapd configuration missing."
[ -f "$DHCP_CONF" ] || fail "DHCP configuration missing."
[ -f "$ROOT_DIR/config/dns/dns.conf" ] || fail "DNS configuration missing."
[ -f "$FIREWALL_CONF" ] || fail "Firewall configuration missing."

pgrep -x hostapd >/dev/null && fail "hostapd is already running. Stop the manual test first."
pgrep -x dnsmasq >/dev/null && fail "dnsmasq is already running. Stop the manual test first."

ip link show "$UPLINK" >/dev/null 2>&1 || fail "$UPLINK not found."

DEFAULT_IF=$(ip route show default | awk 'NR==1 {for(i=1;i<=NF;i++) if($i=="dev") print $(i+1)}')
[ "$DEFAULT_IF" = "$UPLINK" ] || fail "Firewall expects $UPLINK, but default interface is $DEFAULT_IF."

FREQ=$(iw dev "$UPLINK" link | awk '/freq:/ {print int($2); exit}')
[ -n "$FREQ" ] || fail "Upstream Wi-Fi is not connected."

[ "$FREQ" -ge 2412 ] && [ "$FREQ" -le 2472 ] || fail "This configuration currently supports a 2.4 GHz upstream Wi-Fi connection."

CHANNEL=$(( (FREQ - 2407) / 5 ))

mkdir -p "$RUN_DIR" "$LOG_DIR" /run/hostapd

echo "[1/6] Preparing access point..."

if ! iw dev "$AP" info >/dev/null 2>&1; then
    iw dev "$UPLINK" interface add "$AP" type __ap
fi

ip link set "$AP" down || true

AP_TYPE=$(iw dev "$AP" info | awk '/type/ {print $2; exit}')
if [ "$AP_TYPE" != "AP" ]; then
    iw dev "$AP" set type __ap
fi

ip link set "$AP" address 02:11:22:33:44:50
ip addr replace 192.168.50.1/24 dev "$AP"
ip link set "$AP" up

echo "[2/6] Enabling packet forwarding..."
sysctl -w net.ipv4.ip_forward=1

echo "[3/6] Applying firewall and logging rules..."
iptables-restore --test < "$FIREWALL_CONF"
iptables-restore < "$FIREWALL_CONF"

echo "[4/6] Preparing hostapd..."
sed "s/^channel=.*/channel=$CHANNEL/" "$HOSTAPD_CONF" > "$RUN_DIR/hostapd.conf"

if ! grep -q '^ctrl_interface=' "$RUN_DIR/hostapd.conf"; then
    echo "ctrl_interface=/run/hostapd" >> "$RUN_DIR/hostapd.conf"
fi

chmod 600 "$RUN_DIR/hostapd.conf"

hostapd -B \
    -P "$RUN_DIR/hostapd.pid" \
    -f "$LOG_DIR/hostapd.log" \
    "$RUN_DIR/hostapd.conf"

sleep 2
[ -s "$RUN_DIR/hostapd.pid" ] || fail "hostapd did not start."

echo "[5/6] Starting DHCP and DNS..."

DNS_CONF="$ROOT_DIR/config/dns/dns.conf"
DNSMASQ_CONF="$RUN_DIR/dnsmasq.conf"

cat "$DHCP_CONF" "$DNS_CONF" > "$DNSMASQ_CONF"

dnsmasq --test --conf-file="$DNSMASQ_CONF"
dnsmasq --conf-file="$DNSMASQ_CONF" --pid-file="$RUN_DIR/dnsmasq.pid"

sleep 2
[ -s "$RUN_DIR/dnsmasq.pid" ] || fail "dnsmasq did not start."

echo "[6/6] Checking router..."
sysctl net.ipv4.ip_forward
iw dev "$AP" info | grep 'type AP'
ip -br addr show "$AP"

echo "Virtual Router startup completed."
