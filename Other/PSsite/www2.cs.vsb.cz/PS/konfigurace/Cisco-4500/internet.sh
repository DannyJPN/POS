#!/bin/sh
if [ -z "$1" -o "$1" = "-h" ] ; then
  echo Usage: $0 interface 
  exit 1
fi
ifconfig $1 50.0.0.12 netmask 255.255.255.0
# Nova varianta
route add -net 30.0.0.0 netmask 255.255.255.0 gw 50.0.0.1
 
# Stara varianta
#ifconfig ${1}:1 30.0.0.10 netmask 255.255.255.0
#/etc/init.d/thttpd start

/etc/init.d/ssh start
sed '1arecursion yes;' /etc/bind/named.conf.options > /tmp/no$$
mv /tmp/no$$ /etc/bind/named.conf.options
/etc/init.d/bind9 start
