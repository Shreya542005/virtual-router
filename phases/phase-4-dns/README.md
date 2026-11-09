# Phase 4 – DNS Configuration

DNS was implemented using `dnsmasq` and integrated with the existing DHCP service.

## Configuration:**
- Upstream DNS: `1.1.1.1`, `8.8.8.8`
- DNS cache: 1000 entries
- Local domain: `router.lan`
- Router DNS address: `192.168.50.1`

## Practical Tests:**
- Configuration validation using `dnsmasq --test`
- Local DNS resolution using `nslookup router.lan`
- External DNS resolution using `nslookup example.com`

## Result:
 DNS forwarding, caching and local name resolution were successfully configured and tested.