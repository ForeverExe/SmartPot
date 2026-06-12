import board
import time
import adafruit_dht
from analogio import AnalogIn
from digitalio import DigitalInOut, Direction, Pull

DHT_PIN = board.GP14
LED_PIN = board.LED #board.GP13
SOIL_PIN = board.GP28_A2
LED_PIN = board.GP5

dht = adafruit_dht.DHT11(DHT_PIN)
soil_device = AnalogIn(SOIL_PIN)
led = DigitalInOut(LED_PIN)
led.direction = Direction.OUTPUT

def get_voltage(pin):
    return (pin.value * 3.3) / 65536

while True:
    try:
        temperature = dht.temperature
        humidity = dht.humidity
        soil_hum = soil_device.value
        print(f"DHT: Temp: {temperature} - Air Humidity: {humidity}")
        print(f"Capacitive Soil Humidity: {soil_hum}")
        if(soil_hum < 2):
            led.value(True)
            time.sleep(5)
            led.value(False)
        time.sleep(1)
    except RuntimeError as e:
        # Reading doesn't always work! Just print error and we'll try again
        print("Reading from DHT failure: ", e.args)