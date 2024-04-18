#!/bin/sh
if [ -z "$1" -o "$1" = "-h" ] ; then
  echo Usage: $0 interface 
  exit 1
fi
ifconfig eth0 0.0.0.0
route del default
echo > /etc/resolv.conf
ifconfig $1 40.0.0.11 netmask 255.255.255.0
# Nova varianta
route add -net 30.0.0.0 netmask 255.255.255.0 gw 40.0.0.1
route add default gw 40.0.0.1
 
# Stara varianta
#ifconfig ${1}:1 30.0.0.10 netmask 255.255.255.0
#/etc/init.d/thttpd start

/etc/init.d/ssh start
/etc/init.d/inetd start
#sed '1arecursion yes;' /etc/bind/named.conf.options > /tmp/no$$
#mv /tmp/no$$ /etc/bind/named.conf.options
#/etc/init.d/bind9 start
