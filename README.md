# Smart Pot: it's not really a pot, but it's smart

**Inserire immagine 800x800**

# Summary

The Smart Pot is a project done for @piconem's Intelligent Internet of Things class at Universiá di Modena e
Reggio Emilia (UniMoRe) - Sede di Mantova.  
The device is a support for any plant that wants to be monitored, you just need to place the vase
inside it and to position both the sensors and actuators.

At its actual state, it can track and send via MQTT the plant's telemetry, its information for discovery and update its
settings by using a dedicated topic which it's subscribed to.

On the other side, a desktop Java application runs as a hub to list which devices have been discovered, read their telemetry
and update their settings.

# Bill of Materials

- 1x Raspberry Pi Pico 2W (or any other kind of pico, as long it can be connected to a network)
- 1x Capacitive Soil Humidity Sensor (v1.2 in this case)
- 1x DHT11 Air Humidity and Temperature sensor
- Cables as needed
- 1x Perfboard (around 5x7cm)
- 1x Green LED (substitutes)
- 1x 1k Ohm Resistor
- 1x Printed SmartPot (Tolerances not accounted for)

The print can be done in any printer with a bed greater or equal 256^3 cm, just make sure you print it with some wear/weather resistant material like PETG or ASA/ABS.  
The model has been made using FreeCAD and then sliced in OrcaSlicer.

---

# Structure

The project is basically divided in two parts:

- The CircuitPython application which runs on the Raspberry pico
- The Java desktop app

### The CircuitPython device

The SmartPot device is running with one of the latest CircuitPython releases, which has been loaded and programmed with
"Thonny". This way, it can also support the Raspberry Pi Pico W (the non W-versions need an external adapter,
which the code does not contemplate).
The main libraries that are being used for this project are all coming from adafruit's official and community packages,
especially:

- miniMQTT
- DHT
- SenML
- analogio

The CircuitPython logic runs on three files:

- **code.py**: Main logic file, manages the MQTT connection and both transmission and receive of data. Data like credentials, etc, are taken from the `settings.yaml` file contained inside the CircuitPython environment.
- **Sensors.py**: A class which contains all the logic regarding both sensors and actuators initialization while also giving helper functions to read from the sensors.
- **Settings.py**: A class which represents the device's triggers and timers. Has getter and setters for each one of them to be used when receiving new settings from the Java app or when publishing them to the broker.

If debugging or more details are needed, the device prints in Thonny's stdout.

The Device reads and prepares a Json+SenMLPack containing every telemetry available and publishes it to the `t` topic.
It also prepares and publishes via Json+SenMLRecord the single telemetry values so they could be obtained singularily (not used in the app for simplicity.)

---

### The Java Application

The Java application, other than the standard library, only uses Google's `Gson` and Eclipse's `Paho MQTT`.
It can read and create SenML records and packs, but it uses a makeshift version. Since it follows the standard, it can communicate seamlessly with Adafruit's SenML.

A TUI menu enables the user to easily navigate the options given:

- Read the list of connected device
- Read and change the settings of the selected device
- Read the selected device's telemetry

At startup, it subscribes to the device's Settings, Info and Telemetry topics to start adding the device to the internal list and synchronize their information and settings. After that it will start updating their telemetry values and unsubscribe to the Settings topic in order to publish on it later.

##### Java structure

- **SmartPotDescriptor**: Object which represents a single SmartPot, containing general information like model, version, name and the
device's uuid. Also contains the settings and telemetry objects, so everything is being accessed through this.
- **SmartPotSettings**: A Basic POJO containing all the settings used by the device. It gets synchronized at startup via subscribe and then unsubscribes after finishing syncing.
- **SmartPotTelemetry**: POJO used to store the device's telemetry, it gets updated frequently, based on the device's "refreshTimer" option.

- **SmartPotServer**: The name of the app which uses the aformentioned objects, it connects to the broker, creates the devices' list, prepares and responds to user interaction thanks to the TUI.
  

- **DeviceSim** is a partial device simulator that only sends information data
---

# The MQTT Structure

MQTT is the communication method chosen for the project. It uses the professor's Mosquitto instance as a broker and it's divided by
the device's UUID, which is not available per-se in circuitpython, so it uses the ID of the device's CPU.

```
root  
├── uuid1  
│   ├── i (Device Information)
│   ├── s (Device Settings)  
│   └── t (Device Telemetry)  
│       ├── ah (Air Humidity)  
│       ├── sh (Soil Humidity)  
│       └── tmp (Air Temperature)  
├── uuid2  
├── ...  
└── uuidN  
```

| Topic | Pub | Sub | QOS | Retained |
| ----| ----  | --- | --- |  ---    |
|uuid | Pico  | App | 1   |  true  |
| i   | Pico  | App |   1 |  true  |
| s   | App   | Pico|   1 |  true  |
| t   | Pico  | App |  0  |  false |
| ah  | Pico  | App |  0  |  false |
| sh  | Pico  | App | 0   |  false |
| tmp | Pico  | App | 0   |  false |

(For simplicity, "ah", "sh" and "tmp" are not implemented in the app logic)

# Known Issues

- Messages used for discovery (device info) and settings should be sent with a QOS 2 for major certainty of correct message delivery, but *QOS 2 is not yet implemented in adafruit's publish method*.
- The model has some tolerance issues in the area dedicated to slot the electronics. The areas are needed to be increased in size or lightly milled in order to fit.

# Gallery

***TODO: Inserire video di dimostrazione***

