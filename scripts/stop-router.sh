#!/bin/bash
set -e

[ "$(id -u)" -eq 0 ] || {
    echo "Please run with sudo."
    exit 1
}

RUN_DIR="/run/virtual-router"

echo "Stopping Virtual Router..."

# Stop hostapd
if [ -f "$RUN_DIR/hostapd.pid" ]; then
    kill "$(cat "$RUN_DIR/hostapd.pid")" 2>/dev/null || true
    rm -f "$RUN_DIR/hostapd.pid"
fi

# Stop dnsmasq
if [ -f "$RUN_DIR/dnsmasq.pid" ]; then
    kill "$(cat "$RUN_DIR/dnsmasq.pid")" 2>/dev/null || true
    rm -f "$RUN_DIR/dnsmasq.pid"
fi

sleep 1

# Remove project firewall rules
iptables -D INPUT -i ap0 -j VR_INPUT 2>/dev/null || true
iptables -D FORWARD -j VR_FORWARD 2>/dev/null || true
iptables -t nat -D POSTROUTING -o wlp2s0 -j MASQUERADE 2>/dev/null || true

for chain in VR_INPUT VR_FORWARD VR_LOG; do
    iptables -F "$chain" 2>/dev/null || true
    iptables -X "$chain" 2>/dev/null || true
done

iptables -P FORWARD ACCEPT

# Disable packet forwarding
sysctl -w net.ipv4.ip_forward=0

# Remove AP interface
if ip link show ap0 >/dev/null 2>&1; then
    ip link set ap0 down
    iw dev ap0 del
fi

echo "Virtual Router stopped."
