import wifi

wifi.radio.connect("FRITZ!Box 7530 NM 2,4GHz", "ventinove@11")
print("Connected to Wifi!")
print("My IP addr:", wifi.radio.ipv4_address)