# Phase 7 – Integration

## What I did

In this phase, I started bringing the different components of the virtual router together.

Until now, I had configured and tested the wireless access point, DHCP, DNS, packet forwarding, firewall and logging separately.

The purpose of this phase was to make it easier to start these services together instead of configuring each one manually.

## 1. Startup Script

I created a shell script called `start-router.sh` inside the `scripts` directory.

This script performs the following operations:

1. Checks the required configuration files and network interface.
2. Creates and prepares the virtual wireless interface `ap0`.
3. Assigns the router IP address `192.168.50.1/24`.
4. Enables IP forwarding.
5. Loads firewall and logging rules.
6. Starts the wireless access point using hostapd.
7. Starts DHCP and DNS using dnsmasq.

The script also checks whether the upstream Wi-Fi connection is available.

## 2. DHCP and DNS Integration

Both DHCP and DNS are handled by dnsmasq.

Their settings are maintained in separate configuration files:

- `config/dhcp/ap-dhcp.conf`
- `config/dns/dns.conf`

The startup script combines these files into a temporary configuration under `/run/virtual-router/` before starting dnsmasq.

## 3. Shutdown Script

I also created `stop-router.sh` to stop the hostapd and dnsmasq processes, remove the project's firewall rules, disable IP forwarding and remove the virtual AP interface.

The shutdown script has been created, but its complete behaviour still needs testing.

## 4. Running the Router

The startup script can be executed from the project directory using:

```bash
sudo bash scripts/start-router.sh
```

To stop the router:

```bash
sudo bash scripts/stop-router.sh
```

## 5. Testing and Current Status

During an earlier startup test, the script successfully:

- Prepared the AP interface.
- Enabled IP forwarding.
- Loaded firewall rules.
- Started hostapd.
- Started dnsmasq.

The phone connected to the virtual router, received an IP address and accessed `example.com`.

I also updated the startup script to combine the DHCP and DNS configuration files. The combined dnsmasq configuration passed its syntax check.

The complete startup process with this latest change still needs a final verification.

## 6. Java CLI

A Java-based command-line interface is also included in the project.

The CLI is intended to provide commands for starting, stopping and checking router services. Its complete integration with the startup and shutdown scripts still needs verification.

## 7. Current Limitations

- The project currently assumes the wireless interfaces `wlp2s0` and `ap0`.
- The upstream connection and virtual AP share one wireless adapter, so upstream channel changes may interrupt the AP.
- The shutdown script needs a complete test.
- Direct testing of firewall blocking behaviour is pending.
- Full Java CLI integration is not yet verified.

## Result

The individual networking services have been configured and tested, and a startup script has been developed to bring them together.

Further testing is required to verify the complete lifecycle and command-line management of the virtual router.
