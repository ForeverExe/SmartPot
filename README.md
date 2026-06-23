# Smart Pot: it's not really a pot, but it's smart

**Inserire immagine 800x800**

# Summary
The Smart Pot is a project done for @piconem's Intelligent Internet of Things class at Universiá di Modena e 
Reggio Emilia (UniMoRe).
The device is a support for any vase with plants inside that want to be monitored, you just need to place the vase 
inside it and to position the sensors and actuators where it's more comfortable.

At its actual state, it can track and send via MQTT the plant's telemetry, its information for discovery and update its
settings by using a dedicated topic which it's subscribed to.

On the other side, a desktop Java application runs as a hub to list which devices have been discovered, read their telemetry
and update their settings.

# Bill of Materials
- 1x Raspberry Pi Pico 2W (or any other kind of pico, as long it can be connected to a network)
- 1x Capacitive Soil Humidity Sensor (1.2 in this case)
- 1x DHT11 Air Humidity and Temperature sensor
- Cables as needed
- 1x perfboard (around 5x7cm)

# Structure
The project is basically divided in two parts:
- The CircuitPython application which runs on the Raspberry pico
- The Java desktop app

## The CircuitPython device
The SmartPot device is running with one of the latest CircuitPython releases, which has been loaded and programmed with 
"Thonny". This way, it can support also the Raspberry Pi Pico W (the non W-versions need an external adapter, 
which the code does not contemplate).
The main libraries that are being used for this project are all coming from adafruit's official and community package,
especially:
- miniMQTT
- DHT
- SenML
- analogio

The CircuitPython logic runs on three files:
- code.py, it has the main logic of the device, manages the connection to the MQTT Broker and both transmission and receive.
- Sensors.py, a class which contains all the logic regarding both sensors and actuators initialization, while also giving
helper functions to read from the sensors.
- Settings.py, a class which represents the device's triggers and timers. Has getter and setters for each one of them to be used
when receiving new settings from the Java app or when publishing them to the broker.

## The Java Application
The Java application, other than the standard library, only uses Google's Gson and Eclipse's Paho MQTT.
It can read and create SenML records and packs, but it uses a makeshift version. Since it's a standard, it can communicate
seamlessly with Adafruit's SenML.

- Read the list of connected device
- Read and change the settings of the selected device
- REad the selected device's telemetry