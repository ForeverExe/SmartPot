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
import random

from PotSettings import PotSettings
from senml import *

############## VARIABLES AND ALMOST-CONSTANTS #############
ssid = getenv("WIFI_SSID")
#ssid = getenv("TEL_SSID")
password = getenv("WIFI_PASSWORD")
#password = getenv("TEL_PASSWD")

mqtt_username = getenv("MQTT_USERNAME")
mqtt_password = getenv("MQTT_PASSWORD")
mqtt_ip = getenv("MQTT_IP")
mqtt_port = int(getenv("MQTT_PORT"))

uuid = microcontroller.cpu.uid.hex()
print(f"UUID using the processor hex'd UID: {uuid}")

basic_topic = getenv("MQTT_BASIC_TOPIC")+'/'+uuid #--> /device/<uuid>
print(f"Basic topic of the device: {basic_topic}")

subscribe_feed = basic_topic+getenv("MQTT_DEVICE_SETTINGS_TOPIC") #--> /device/<uuid>/settings
tel_topic = basic_topic+getenv("MQTT_BASE_TELEMETRY") #--> /device/<uuid>/telemetry
info_topic = basic_topic+getenv("MQTT_DEVICE_INFO_TOPIC") #--> /device/<uuid>/info
print("Main Topics:")
print(f"Settings: {subscribe_feed}\nTelemetry: {tel_topic}\nInformation: {info_topic}\n")

sh_topic = tel_topic+getenv("MQTT_TEL_SH")
ah_topic = tel_topic+getenv("MQTT_TEL_AH")
temp_topic = tel_topic+getenv("MQTT_TEL_TEMP")
water_topic = tel_topic+getenv("MQTT_TEL_WATER")
print("Telemetry Topics:")
print(f"- {sh_topic}\n- {ah_topic}\n- {temp_topic}\n- {water_topic}\n")

device_info ={'name':'SmartPot Proto1','id':uuid,'version':'0.0.2','type':'Raspberry Pi Pico2W'}
json_info = json.dumps(device_info, separators=(',',":"))
print(f"Info:\n{device_info}")
settings = PotSettings()
print(f"Settings:\n{settings}")

############# LOGIC #############

### MQTT Client methods
def connected(client, userdata, flags, rc):
    print("Done! Publishing the device informations...")
    print(str(rc)+"\n\n")
    mqtt_client.publish(info_topic, json_info, True, 1)

def disconnected(client, userdata, rc):
    print("Disconnected from the Broker")
    
def message(client, topic, message):
    print(f"New message on topic {topic}: {json.loads(message)}")

def apply_settings_msg(client, topic, message):
    print("ooooh ho ricevuto qualcosa sul topic dei settings!")
    print(str(message))

#### Pot Methods
def update_and_send():
    try:
        print("TempRecord")
        #1 pack, 4 record, tutte da pubblicare
        tpack = SenmlPack("telemetry")
        ah = SenmlRecord("air_hum", unit=SenmlUnits.SENML_UNIT_RELATIVE_HUMIDITY, value=random.uniform(10.0,40.0), time=time.time())
        sh = SenmlRecord("soil_hum", unit=SenmlUnits.SENML_UNIT_RELATIVE_HUMIDITY, value=random.uniform(20.0,50.0),time=time.time())
        temp = SenmlRecord("temperature", unit=SenmlUnits.SENML_UNIT_DEGREES_CELSIUS, value=random.randint(0,40), time=time.time())
        watime = SenmlRecord("water", unit=SenmlUnits.SENML_UNIT_LITER_PER_SECOND, value=random.random(), time=time.time())
        tpack.add(ah)
        tpack.add(sh)
        tpack.add(temp)
        tpack.add(watime)
        jsonOut = tpack.to_json()
        
        jstruct = json.loads(jsonOut)
        print(jstruct[0])
        print(jstruct[1])
        print(jstruct[2])
        print(jstruct[3])
        
        mqtt_client.publish(tel_topic, jsonOut, False, 0)
        mqtt_client.publish(ah_topic, json.dumps(jstruct[0]), False, 0)
        mqtt_client.publish(sh_topic, json.dumps(jstruct[1]), False, 0)
        mqtt_client.publish(temp_topic, json.dumps(jstruct[2]), False, 0)
        mqtt_client.publish(water_topic, json.dumps(jstruct[3]), False, 0)
    except (ValueError, RuntimeError) as e:
        print("Error publishing the data...\n", e)
        
    
#### MAIN
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
    client_id = "Pot"+uuid,
    socket_pool = pool,
    ssl_context = ssl_context,
    use_binary_mode = True,
)

logger = mqtt_client.enable_logger(adafruit_logging, adafruit_logging.WARNING, "loggy")

mqtt_client.on_connect = connected
mqtt_client.on_disconnect = disconnected
mqtt_client.add_topic_callback(subscribe_feed, apply_settings_msg)
try:
    print("Connecting to the broker...")
    mqtt_client.connect()
        
    print("Connection Established: subscribing and listening to the settings topic...")
    mqtt_client.subscribe(subscribe_feed, 2)
except (ValueError, RuntimeError) as e:
    print("Failed to connect, retrying")
    wifi.reset()
    mqtt_client.reconnect()
    

while True:
    try:
        update_and_send()
    except (ValueError, RuntimeError) as e:
        print("Failed to get data, retrying\n", e)
        wifi.reset()
        mqtt_client.reconnect()
        continue
    time.sleep(30)