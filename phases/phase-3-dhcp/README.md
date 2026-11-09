# Phase 3 – DHCP Service

## Objective
Implement automatic IP address allocation for devices connected to the virtual router's wireless Access Point.

## Implementation
- DHCP server: dnsmasq
- Interface: `ap0`
- Router IP: `192.168.50.1/24`
- DHCP pool: `192.168.50.10 – 192.168.50.100`
- Default gateway: `192.168.50.1`
- DNS server: `8.8.8.8`
- Lease duration: 12 hours

## Configuration
DHCP settings are maintained in `config/dhcp/ap-dhcp.conf`.

dnsmasq listens on `ap0` and dynamically assigns network configurations to connected clients.

## Testing
Tested with an external mobile phone connected to `VirtualRouter`.

- Client: `shivam-ka-phone`
- Assigned IP: `192.168.50.45`
- DHCP acknowledgement: Successful

-implemented DHCP address allocation for external devices connected to the virtual router.