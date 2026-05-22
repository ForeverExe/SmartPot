import analogio
import digitalio
from board import *
import board
import microcontroller
import adafruit_dht

soilhum:analogio.AnalogIn
airhum:analogio.AnalogIn
ledhose:digitalio.DigitalInOut

#sh_power=digitalio.DigitalInOut(board.GP27)
#sh_power.direction = digitalio.Direction.OUTPUT
#sh_power.value=True
#soilhum = analogio.AnalogIn(board.GP26_A0)


#led = digitalio.DigitalInOut(board.LED)
#led.direction = digitalio.Direction.OUTPUT
#led.value=True

#while(True):
    #temp = (soilhum.value * 222.2) - 61.111 #necessita dei 5v
    #print(f"Temperature: {temp}")
    #print(f"Soilhum: {soilhum.value}")
    #print(f"Led: {led.value}")
    #led.value = not led.value
    #time.sleep(1)

def init_soilhum(gpio:microcontroller.Pin):
    soilhum = analogio.AnalogIn(gpio)
    
def init_airhum(gpio:microcontroller.Pin):
    airhum = adafruit_dht.DHT11(gpio)
    
def init_waterled(gpio:microcontroller.Pin):
    ledhose = digitalio.DigitalInOut(gpio)
    ledhose.direction = digitalio.Direction.OUTPUT


def read_soil_hum():
    return temp = (soilhum.value * 222.2) - 61.111 #necessita dei 5v

def read_air_hum():
    