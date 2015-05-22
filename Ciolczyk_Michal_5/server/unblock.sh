sudo iptables -D INPUT --src 149.156.97.154 -j REJECT
sudo iptables -D OUTPUT --dst 149.156.97.154 -j REJECT
sudo iptables -D INPUT --src 149.156.97.154 -j DROP
sudo iptables -D OUTPUT --dst 149.156.97.154 -j DROP
