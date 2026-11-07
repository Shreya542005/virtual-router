# Phase 2 - Packet Forwarding

## What I did

In this phase, I started working on connecting an external device (mobile phone) to my Linux laptop and using the laptop as a virtual router.

Instead of testing only with network namespaces, I tried creating a real Wi-Fi access point using my laptop's wireless adapter.

## 1. Creating a virtual interface

First, I created a new wireless interface called `ap0`.

```bash
sudo iw dev wlp2s0 interface add ap0 type __ap
```

Then I checked the interface and changed its mode to AP.

```bash
sudo iw dev ap0 set type __ap
iw dev ap0 info
```

The interface was successfully detected in AP mode.

## 2. Configuring the access point

I installed and used `hostapd` to create my own Wi-Fi network.

Configuration file: `config/hostapd/hostapd.conf`

The Wi-Fi network name (SSID) is `VirtualRouter`.

I configured WPA2 security and started the access point using:

```bash
sudo hostapd -dd config/hostapd/hostapd.conf
```

The terminal displayed:

```text
ap0: AP-ENABLED
ap0: Setup of interface done.
```

This confirmed that the access point started successfully.

## 3. Assigning an IP address

I assigned a separate IP address to the virtual interface.

```bash
sudo ip addr add 192.168.50.1/24 dev ap0
sudo ip link set ap0 up
```

Checked using:

```bash
ip addr show ap0
```

Observed IP address: `192.168.50.1/24`

## 4. Connecting an external device

I checked the Wi-Fi list on my mobile phone. The network `VirtualRouter` was visible.

When I tried connecting, the hostapd terminal showed successful authentication and association.

```text
AP-STA-CONNECTED
WPA: pairwise key handshake completed (RSN)
EAPOL-4WAY-HS-COMPLETED
```

The phone subsequently disconnected, so a stable connection is still to be verified.

## 5. Work remaining

- Get a stable connection from the mobile phone.
- Configure DHCP for the connected device.
- Enable IP forwarding in Linux.
- Configure NAT.
- Test communication between the phone and external networks.

## 6. Observation

The laptop successfully created and broadcast its own Wi-Fi network using `hostapd`. The mobile phone could detect the network and complete WPA authentication.

The next step is to configure the remaining networking services so that the laptop can forward traffic from the connected device.
