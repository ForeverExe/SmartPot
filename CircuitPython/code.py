from os import getenv

import adafruit_logging
import time
import board
import digitalio
import adafruit_connection_manager
import wifi
import microcontroller
import json
import adafruit_minimqtt.adafruit_minimqtt as MQTT

from PotSettings import PotSettings
from senML import senML

############## VARIABLES AND ALMOST-CONSTANTS #############
#ssid = getenv("WIFI_SSID")
ssid = getenv("TEL_SSID")
#password = getenv("WIFI_PASSWORD")
password = getenv("TEL_PASSWD")

mqtt_username = getenv("MQTT_USERNAME")
mqtt_password = getenv("MQTT_PASSWORD")
mqtt_ip = getenv("MQTT_IP")
mqtt_port = int(getenv("MQTT_PORT"))

uuid = microcontroller.cpu.uid.hex()
print(f"UUID using the processor hex'd UID: {uuid}")

basic_topic = getenv("MQTT_BASIC_TOPIC")+'/'+uuid #--> /device/<uuid>
print(f"Basic topic of the device: {basic_topic}")

subscribe_feed = basic_topic+getenv("MQTT_DEVICE_SETTINGS_TOPIC")
tel_topic = basic_topic+getenv("MQTT_BASE_TELEMETRY") #--> /device/<uuid>/telemetry
info_topic = basic_topic+getenv("MQTT_DEVICE_INFO_TOPIC")

device_info ={'name':'SmartPot Proto1','id':uuid,'version':'0.0.01','type':'Raspberry Pi Pico 2W'}
json_info = json.dumps(device_info, separators=(',',":"))

settings = PotSettings()
record = senML()

############# LOGIC #############

### MQTT Client methods
def connected(client, userdata, flags, rc):
    print("Done!")
    mqtt_client.publish(info_topic, json_info, True, 2)
    
def disconnected(client, userdata, rc):
    print("Disconnected from the Broker")
    
def message(client, topic, message):
    print(f"New message on topic {topic}: {json.loads(message)}")

def apply_settings_msg(client, topic, message):
    print("ooooh ho ricevuto qualcosa sul topic dei settings!")

#### Pot Methods
def update_and_send():
    record.n = "test"
    record.u = "unit"
    record.v = 1.245
    print(record)
    
#### MAIN

print(settings)
print(f"Topics:\nSettings: {subscribe_feed} - Telemetry: {tel_topic} - Device Info: {info_topic}")


wifi.radio.connect(ssid, password)
print("Connected to Wifi!")
print("My IP addr:", wifi.radio.ipv4_address)
   
pool = adafruit_connection_manager.get_radio_socketpool(wifi.radio)
ssl_context = adafruit_connection_manager.get_radio_ssl_context(wifi.radio)

mqtt_client = MQTT.MQTT(
    broker = mqtt_ip,
    port = mqtt_port,
    username = mqtt_username,
    password = mqtt_password,
    client_id=uuid,
    socket_pool = pool,
    ssl_context = ssl_context
)
logger = mqtt_client.enable_logger(adafruit_logging, adafruit_logging.DEBUG, "loggy")

mqtt_client.on_connect = connected
mqtt_client.on_disconnect = disconnected
mqtt_client.add_topic_callback(subscribe_feed, apply_settings_msg)
print("Connecting to the broker...")
mqtt_client.connect(True)

if(mqtt_client.is_connected()):
    print("Connected to the Broker, sending device information...")
    mqtt_client.publish(info_topic, json_info, True, 2)
else:
    print("Connection Error: make sure the credentials are correct.")
    
print("Connection Established: subscribing and listening to the settings topic...")
#mqtt_client.subscribe(subscribe_feed, 2)

while True:
    try:
        mqtt_client.loop(5)
        mqtt_client.publish(info_topic, json_info, True, 2)
        #update_and_send()
    except (ValueError, RuntimeError) as e:
        print("Failed to get data, retrying\n", e)
        wifi.reset()
        mqtt_client.reconnect()
        continue
    time.sleep(1)