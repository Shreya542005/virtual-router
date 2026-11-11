# Phase 2 - Packet Forwarding

## What I did

In this phase, I worked on connecting an external device (mobile phone) to my Linux laptop and using the laptop as a virtual router.

Instead of testing only with network namespaces, I tried creating a real Wi-Fi access point using my laptop's wireless adapter.

## 1. Creating a virtual interface

First, I created a new wireless interface called `ap0`.


sudo iw dev wlp2s0 interface add ap0 type __ap


I checked the interface mode using:


iw dev ap0 info


The interface was successfully created in AP mode.

## 2. Configuring the access point

I used `hostapd` to create my own Wi-Fi network.

- Wi-Fi name (SSID): `VirtualRouter`
- Security: WPA2

The configuration is stored in `config/hostapd/hostapd.conf`. The repository also contains an example configuration.

When I started hostapd, the terminal displayed `AP-ENABLED`, which confirmed that the access point started successfully.

## 3. Assigning an IP address

I assigned a separate IP address to the virtual interface.


sudo ip addr add 192.168.50.1/24 dev ap0
sudo ip link set ap0 up


I checked the address using `ip addr show ap0`.

Observed IP address: `192.168.50.1/24`

## 4. Connecting an external device

I connected my mobile phone to the `VirtualRouter` network.

The hostapd log showed successful authentication and connection.

The phone received the IP address `192.168.50.45` through DHCP.

## 5. Enabling packet forwarding and NAT

I enabled IP forwarding in Linux:


sudo sysctl -w net.ipv4.ip_forward=1


I also configured NAT masquerading using iptables so that the connected device could access external networks through my laptop's Wi-Fi connection.

## 6. Testing

I tested the connection using my mobile phone.

- The phone successfully connected to the virtual access point.
- It received an IP address from the router.
- It successfully accessed `example.com`.

This confirmed that the laptop was forwarding client traffic to the upstream network.

## 7. Observation

I was able to use my Linux laptop as a Wi-Fi access point and provide internet connectivity to an external device.

One limitation I observed is that changing the upstream Wi-Fi network or channel can interrupt the virtual access point because both interfaces use the same wireless adapter.