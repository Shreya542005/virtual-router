# Phase 6 – Logging

Implemented DNS and firewall logging for the virtual router.

### What I did
- Enabled DNS query logging using dnsmasq.
- Enabled DHCP activity logging.
- Saved DNS logs in `/var/log/dnsmasq.log`.
- Created a custom `VR_LOG` iptables chain.
- Added rate-limited firewall logging rules.
- Saved firewall logging configuration.

### Verification
- Tested DNS query for google.com.
- Verified DNS query and response logs.
- Checked firewall logging rules.

### Status
Logging configuration completed. External-device testing pending.
