class PotSettings(object):

    def __init__(self, refresh=5, air=70.0, soil=30.0, temp=50.0, timer=15):
        self.refresh_time:int = refresh
        self.air_hum_trigger:float = air
        self.soil_hum_trigger:float = soil
        self.temp_trigger:float = temp
        self.water_timer:int = timer
        
    def set_refresh(refresh:int):
        self.refresh = refresh
    def get_refresh():
        return self.refresh
    
    def set_air_hum_trigger(air:float):
        self.aur_hum_trigger = air
    def get_air_hum_trigger():
        return self.air_hum_trigger
    
    def set_soil_hum_trigger(soil:float):
        self.soil_hum_trigger = soil
    def get_soil_hum_trigger():
        return self.soil_hum_trigger
    
    def set_temp_trigger(temp:float):
        self.temp_trigger = temp
    def get_temp_trigger():
        return self.temp_trigger
    
    def set_water_timer(timer:float):
        self.water_timer = timer
    def get_water_timer():
        return self.water_timer
    
    def __str__(self):
        return f"Triggers:\n    Air Humidity: {self.air_hum_trigger} - Soil Humidity: {self.soil_hum_trigger} - Temperature: {self.temp_trigger}\nTimers:\n    Telemetry Refresh: {self.refresh_time} - Water Timer: {self.water_timer}"