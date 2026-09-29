# learned
-How DHCP works and configured a DHCP server 
-client get IP automatically instead of  setting it manually

# Network
-Router IP: 192.168.50.1/24
-DHCP range: 192.168.50.10 - 192.168.50.100
-DNS: 8.8.8.8
-Network: 192.168.50.0/24

# linux network namespaces used
-clinetA
-clientB
-router
-DHCP runs inside router

-DHCP gives ip addresses automatically
-A dhcp pool is range of ip addresses taht can be given to clients.
-DHCP also provides information like gateway and DNS
-A client can get another IP when lease is no longer being used.