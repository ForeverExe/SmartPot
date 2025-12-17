import time
import board
import digitalio
from os import getenv

import adafruit_connection_manager
import wifi

import adafruit_minimqtt.adafruit_minimqtt as MQTT

led = digitalio.DigitalInOut(board.LED)
led.direction = digitalio.Direction.OUTPUT

#ssid = getenv("CIRCUITPY_WIFI_SSID")
ssid = getenv("TEL_SSID")
#password = getenv("CIRCUITPY_WIFI_PASSWORD")
password = getenv("TEL_PASSWD")

mqtt_username = getenv("MQTT_USERNAME")
mqtt_password = getenv("MQTT_PASSWORD")
mqtt_ip = getenv("MQTT_IP")
mqtt_port = int(getenv("MQTT_PORT"))

basic_topic = getenv("MQTT_BASIC_TOPIC")

print(f"Connecting to Wifi: {ssid}")
wifi.radio.connect(ssid, password)
print(f"Connected to Wifi!")

publish_feed = basic_topic+"/raspberry_pico"
subscribe_feed = basic_topic+"/onoff"

### Logica ###
def connected(client, userdata, flags, rc):
    print(f"Connected to the Broker as {mqtt_username} , subscribing and listening to feed {subscribe_feed}")
    client.subscribe(subscribe_feed)
    
def disconnected(client, userdata, rc):
    print("Disconnected from the Broker")
    
def message(client, topic, message):
    print(f"New message on topic {topic}: {message}")
    if(str(message) == "accendi"):
        led.value = True
    elif(str(message) == "spegni"):
        led.value = False
    elif(str(message) == "asrubale"):
        led.value = not led.value
        time.sleep(0.5)
        led.value = not led.value
        time.sleep(0.5)
        led.value = not led.value
        
    
pool = adafruit_connection_manager.get_radio_socketpool(wifi.radio)
ssl_context = adafruit_connection_manager.get_radio_ssl_context(wifi.radio)

## Da scommentare se ho bisogno di una coppia certificato-chiave SSL
# ssl_context.load_cert_chain(
#     certfile=getenv("device_cert_path"), keyfile=getenv("device_key_path")
# )


## Setup MQTT Client
mqtt_client = MQTT.MQTT(
    broker = mqtt_ip,
    port = mqtt_port,
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


onoff_value = 0
while True:
    mqtt_client.loop(timeout=1)
    
    print(f"Sending a value to the topic {publish_feed}: {onoff_value}")
    mqtt_client.publish(publish_feed, onoff_value)
    print("Sent!")
    onoff_value += 1
    time.sleep(5)