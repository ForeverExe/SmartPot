# SPDX-FileCopyrightText: Copyright (c) 2025 Tim C for Adafruit Industries
#
# SPDX-License-Identifier: Unlicense

from microcontroller import pin
import board
import busio
import displayio
import terminalio
from adafruit_display_text.bitmap_label import Label
from fourwire import FourWire
from vectorio import Circle

from adafruit_gc9a01a import GC9A01A

spi = busio.SPI(clock=board.GP18, MOSI=board.GP19, MISO=board.GP16)
tft_cs = board.GP17
tft_dc = board.GP20
tft_reset = board.GP21

displayio.release_displays()

display_bus = FourWire(spi, command=tft_dc, chip_select=tft_cs, reset=tft_reset)
display = GC9A01A(display_bus, width=240, height=240)

# Make the display context
main_group = displayio.Group()
display.root_group = main_group

bg_bitmap = displayio.Bitmap(240, 240, 2)
color_palette = displayio.Palette(2)
color_palette[0] = 0xf0300a  
color_palette[1] = 0x000000

bg_sprite = displayio.TileGrid(bg_bitmap, pixel_shader=color_palette, x=0, y=0)
main_group.append(bg_sprite)

inner_circle = Circle(pixel_shader=color_palette, x=120, y=120, radius=100, color_index=1)
main_group.append(inner_circle)

# Draw a label
text_group = displayio.Group(scale=2, x=50, y=120)
text = "Hello World!"
text_area = Label(terminalio.FONT, text=text, color=0xFFFF00)
text_group.append(text_area)  # Subgroup for text scaling
main_group.append(text_group)

board.GP18.deinit()
board.GP19.deinit()
board.GP16.deinit()
board.GP17.deinit()
board.GP20.deinit()
board.GP21.deinit()


while True:
    pass
