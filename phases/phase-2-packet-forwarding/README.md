# phase 2 - packet forwarding 

understand and demonstrate how a linux syatem can forward packets between two different network

## concepts learned

Packet forwarding
Routing vs forwarding
ip forwarding
Network namespaces
Basic routing configuration
Packet testing with ping
Packet observation using wireshark


## Topology

client A --> Router --> client B

client A:`192.168.1.10/24`
Router interface 1 :`192.168.1.1/24`
Router interface 2 :`192.168.2.1/24`
client B:`192.168.2.10/24`

-created isolated namespaces clientA ,clientB,Router
-connected the namespaces using veth pairs.
-Assigned ip address to interfaces
-Enable ipv4 packet forwarding in Linux
-Added routes between two networks
-tested connectivity using `ping`
-Observed ICMP traffic using Wireshark   