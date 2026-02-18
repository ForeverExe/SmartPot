import time
import board
import microcontroller
import digitalio
import adafruit_connection_manager
import wifi
import adafruit_minimqtt.adafruit_minimqtt as MQTT
import json

from os import getenv
from senml import *
from PotSettings import PotSettings

ssid = getenv("WIFI_SSID")
#ssid = getenv("TEL_SSID")
password = getenv("WIFI_PASSWORD")
#password = getenv("TEL_PASSWD")

mqtt_username = getenv("MQTT_USERNAME")
mqtt_password = getenv("MQTT_PASSWORD")
mqtt_ip = getenv("MQTT_IP")
mqtt_port = getenv("MQTT_PORT")

####### SETUP #######
uuid = microcontroller.cpu.uid.hex()
print(f"UUID using the processor hex'd UID: {uuid}")

basic_topic = getenv("MQTT_BASIC_TOPIC")+'/'+uuid #--> /device/<uuid>
print(f"Basic topic of the device: {basic_topic}")

subscribe_feed = basic_topic+getenv("MQTT_DEVICE_SETTINGS_TOPIC") #--> /device/<uuid>/settings
tel_topic = basic_topic+getenv("MQTT_BASE_TELEMETRY") #--> /device/<uuid>/telemetry
info_topic = basic_topic+getenv("MQTT_DEVICE_INFO_TOPIC") #--> /device/<uuid>/info
print("Topics:\n")
print(f"Settings: {subscribe_feed}\nTelemetry: {tel_topic}\nInformation: {info_topic}\n")

device_info ={'name':'SmartPot Proto1','id':uuid,'version':'0.0.2','type':'Raspberry Pi Pico2W'}
json_info = json.dumps(device_info, separators=(',',":"))
print(f"Info:\n{device_info}")
settings = PotSettings()
print(f"Settings:\n{settings}")

##### CONNECTION ######

print(f"Connecting to Wifi: {ssid}")
wifi.radio.connect(ssid, password)
print(f"Connected to Wifi!")

#################### Logica #####################
def connected(client, userdata, flags, rc):
    print(f"Connected to the Broker, subscribing and listening to feed {subscribe_feed}")
    client.subscribe(subscribe_feed)
    
def disconnected(client, userdata, rc):
    print("Disconnected from the Broker")
    
def message(client, topic, message):
    print(f"New message on topic {topic}: {message}")
    
pool = adafruit_connection_manager.get_radio_socketpool(wifi.radio)
ssl_context = adafruit_connection_manager.get_radio_ssl_context(wifi.radio)

## Setup MQTT Client
mqtt_client = MQTT.MQTT(
    broker = mqtt_ip,
    port = int(mqtt_port),
    username = mqtt_username,
    password = mqtt_password,
    socket_pool = pool,
    ssl_context = ssl_context,
)


mqtt_client.on_connect = connected
mqtt_client.on_disconnect = disconnected
mqtt_client.on_message = message

print("Connecting to the Broker...")
mqtt_client.connect()

while True:
    try:
        mqtt_client.loop(timeout=1)
    except (ValueError, RuntimeError) as e:
        wifi.reset()
        mqtt_client.reconnect()
        continue
    time.sleep(5)
