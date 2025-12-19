from os import getenv

import time
import board
import digitalio
import adafruit_connection_manager
import wifi
import microcontroller
import builtins
import json
import adafruit_minimqtt.adafruit_minimqtt as MQTT

#ssid = getenv("CIRCUITPY_WIFI_SSID")
ssid = getenv("TEL_SSID")
#password = getenv("CIRCUITPY_WIFI_PASSWORD")
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
### MQTT Client methods
def connected(client, userdata, flags, rc):
    print("Connected to the Broker, sending device information...")
    client.publish(info_topic, json_info, True, 2)
    print("Subscribing and listening to the settings topic...")
    client.subscribe(subscribe_feed)
    print("Done!")
    
def disconnected(client, userdata, rc):
    print("Disconnected from the Broker")
    
def message(client, topic, message):
    print(f"New message on topic {topic}: {message}")

def apply_settings():
    print("ooooh ho ricevuto qualcosa sul topic dei settings!")


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
    socket_pool = pool,
    ssl_context = ssl_context
)

mqtt_client.on_connect = connected
mqtt_client.on_disconnect = disconnected
mqtt_client.add_topic_callback(subscribe_feed, apply_settings)

print("Connecting to the broker...")
mqtt_client.connect()

if(mqtt_client.is_connected()):
    print("Successfully connected to the broker!")
else:
    print("Connection Error: make sure the credentials are correct.")


while True:
    mqtt_client.loop(timeout=1)
    