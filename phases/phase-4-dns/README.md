# Phase 4 – DNS Configuration

## What I did

In this phase, I configured DNS using `dnsmasq`. Since dnsmasq is already being used for DHCP, I used the same service for DNS also.

The purpose was to allow connected devices to resolve website names without directly configuring an external DNS server on each device.

## Configuration

DNS settings are stored in `config/dns/dns.conf`.

Some important settings are:

- DNS interface: `ap0`
- Router DNS address: `192.168.50.1`
- Upstream DNS servers: `1.1.1.1` and `8.8.8.8`
- DNS cache size: 1000 entries
- Local domain: `router.lan`

I also enabled DNS query logging to observe the requests made by connected devices.

## Testing

I checked the configuration using:

```bash
sudo dnsmasq --test --conf-file=config/dns/dns.conf
```

I also tested local and external DNS resolution using:

```bash
nslookup router.lan 192.168.50.1
nslookup example.com 192.168.50.1
```

I then connected my mobile phone to the virtual router and checked the DNS logs.

## Observation

The DNS logs showed queries coming from the connected mobile phone (`192.168.50.45`).

For example, requests for `content-autofill.googleapis.com` were forwarded to an upstream DNS server and responses were received.

This confirmed that DNS requests from the external device were reaching dnsmasq and being forwarded.

## Result

DNS forwarding, caching and local name resolution were configured using dnsmasq. DNS activity could also be observed through the log file `/var/log/dnsmasq.log`.