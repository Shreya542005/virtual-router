# Software-Based Virtual Router

A software-based virtual router built using Linux networking tools and Java.

## About the Project

The aim of this project is to use a Linux system as a virtual router that can manage client network connections and provide basic router services.

The project uses existing Linux networking utilities and a Java-based command-line interface to manage and monitor router functions.

## Main Features

- Wi-Fi Access Point using hostapd
- DHCP for automatic IP address allocation
- DNS configuration and forwarding
- Firewall rules for network traffic control
- IP forwarding and NAT
- Network and DNS activity logging
- Java-based command-line interface

## Project Structure

- `config/` – Configuration files for router services
- `phases/` – Work organised by development phase
- `routerCLI.java` – Java command-line interface
- `docs/` – Project documentation and architecture

## Development Phases

1. Linux Networking
2. Packet Forwarding
3. DHCP Configuration
4. DNS Configuration
5. Firewall Configuration
6. Network and DNS Logging
7. Integration and CLI

## Technologies Used

- Linux (Ubuntu)
- Java
- hostapd
- dnsmasq
- iproute2
- iptables

## Project Status

The project is under development. Features are being implemented and tested phase by phase.

## Repository

This repository contains the source code, configuration examples, test records and documentation developed during the project.
