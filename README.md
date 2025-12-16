# Smart Pot: it's not really a pot, but it's smart

Smart Pot is a Prototype IoT Project for the Intelligent Internet of Things class of the Università degli Studi di Modena e Reggio
Emilia - Sede di Mantova 

The device is a 3D Printed saucer made of PETG with a surface of TPU placed under any kind of vase or pot you want that 
tracks temperature and humidity of your plant thanks to the sensor included with the piece.  
The main processor is a Rasperry Pi Pico 2 W with a two Humidity sensors, one for Soil and one for Air,  
a temperature sensor and an LED representing the irrigation system (I wanted to put a water pump in it but it  
would end up badly)  

The software is divided in two parts:
- A server running in Java-Maven using Eclipse's Paho-MQTT
- The Pico2W runs CircuitPython with Adafruit's SimpleMQTT Library