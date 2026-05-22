import analogio
import digitalio
from board import *
import board

sh_power=digitalio.digitalOut(board.GP27)
sh_power.value=True
soilhum = analogio.AnalogIn(board.GP26)
print(soilhum.value)

led = digitalio.DigitalInOut(board.LED)
led.value = True
