import analogio
import digitalio
from board import *
import microcontroller

soilhum = analogio.AnalogIn(microcontroller.Pin.A1)
print(soilhum.value)
soilhum.deinit()

led = digitalio.DigitalInOut(board.LED)
led.value = True
