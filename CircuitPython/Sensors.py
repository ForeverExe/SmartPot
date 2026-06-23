import board
import time
import adafruit_dht
from analogio import AnalogIn
from digitalio import DigitalInOut, Direction, Pull

class Sensors(object):

    def __init__(self, dht_pin, soil_pin, led_pin):
        self.DRY_VALUE = 65535
        self.WET_VALUE = 40000
        self.dht = adafruit_dht.DHT11(dht_pin)
        self.soil_device = AnalogIn(soil_pin)
        self.led = DigitalInOut(led_pin)
        self.led.direction = Direction.OUTPUT
        self.led.value = False

    def hum_percent(pin, campioni):
        somma = 0
        for _ in range(campioni):
            somma = somma + pin.value
            time.sleep(0.005)  # Breve pausa tra i campioni
        
        somma= somma//campioni
        
        #print(f"Debug: Voltaggio {(somma*3.33)/65535}")
        
        perc = ((DRY_VALUE - somma) / (DRY_VALUE - WET_VALUE)) * 100
        if perc < 0:
            return 0
        elif perc > 100:
            return 100
        
        return round(perc,1)

    def get_air_hum():
        return self.dht.humidity

    def get_temp():
        return self.dht.temperature

    def get_soil_hum():
        return hum_percent(soil_device, 20)

    def activate_led():
        self.led.value = True
        return self.led.value
    
    def deactivate_led():
        self.led.value = False
        return self.led.value