sudo iptables -I INPUT 1 --src 149.156.97.154 -j REJECT
sudo iptables -I OUTPUT 1 --dst 149.156.97.154 -j REJECT
