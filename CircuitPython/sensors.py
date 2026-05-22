import analogio
import digitalio
from board import *
import board
import time


sh_power=digitalio.DigitalInOut(board.GP27)
sh_power.direction = digitalio.Direction.OUTPUT
sh_power.value=True
soilhum = analogio.AnalogIn(board.GP26_A0)


led = digitalio.DigitalInOut(board.LED)
led.direction = digitalio.Direction.OUTPUT
led.value=True

while(True):
    #temp = (soilhum.value * 222.2) - 61.111 #necessita dei 5v
    #print(f"Temperature: {temp}")
    print(f"Soilhum: {soilhum.value}")
    print(f"Led: {led.value}")
    led.value = not led.value
    time.sleep(1)