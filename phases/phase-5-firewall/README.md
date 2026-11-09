# Phase 5 – Firewall

Configured firewall rules using Linux `iptables`.

### What I did
- Created a custom `VR_INPUT` chain for incoming traffic.
- Allowed DHCP, DNS, Ping and established connections.
- Blocked other incoming traffic from `ap0`.
- Created a custom `VR_FORWARD` chain.
- Allowed client-to-internet and returning traffic.
- Set the default FORWARD policy to DROP.
- Configured NAT masquerading through `wlp2s0`.

### Configuration
Firewall rules are saved in `config/firewall/firewall-rules.v4`.

### Verification
Checked firewall rules and packet counters using `iptables`.

### Status
Firewall configuration completed. External-device testing pending.
