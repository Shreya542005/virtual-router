# Phase 3 – DHCP Service

## Objective
configured DHCP so that devices connected to my virtual Wi-Fi network could automatically receive their IP address and other network settings.


## Implementation
- DHCP server: dnsmasq
- Interface: `ap0`
- Router IP: `192.168.50.1/24`
- DHCP pool: `192.168.50.10 – 192.168.50.100`
- Default gateway: `192.168.50.1`
- DNS server given to clients: 192.168.50.1
- Lease duration: 12 hours

## Configuration
DHCP settings are maintained in `config/dhcp/ap-dhcp.conf`.

dnsmasq listens on `ap0` and dynamically assigns network configurations to connected clients.

## Testing
Tested with an external mobile phone connected to `VirtualRouter`.

- Client hostname: `shivam-ka-phone`
- Assigned IP: `192.168.50.45`
- DHCP acknowledgement: Successful

## Observation

The mobile phone received its network configuration automatically without manually setting an IP address.

The router IP (192.168.50.1) is also provided as the client's DNS server because dnsmasq handles both DHCP and DNS in this project.
