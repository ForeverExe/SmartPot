class PotSettings(object):

    def __init__(self, refresh=5, air=70.0, soil=30.0, temp=50.0, timer=15):
        self.refreshTimer:int = refresh
        self.ahTrigger:float = air
        self.shTrigger:float = soil
        self.tempTrigger:float = temp
        self.waterTimer:int = timer
        
    def set_refresh(self, refresh:int):
        self.refreshTimer = refresh
    def get_refresh(self):
        return self.refreshTimer
    
    def set_air_hum_trigger(self, air:float):
        self.ahTrigger = air
    def get_air_hum_trigger(self):
        return self.ahTrigger
    
    def set_soil_hum_trigger(self, soil:float):
        self.shTrigger = soil
    def get_soil_hum_trigger(self):
        return self.shTrigger
    
    def set_temp_trigger(self, temp:float):
        self.tempTrigger = temp
    def get_temp_trigger(self):
        return self.tempTrigger
    
    def set_water_timer(self, timer:float):
        self.waterTimer = timer
    def get_water_timer(self):
        return self.waterTimer
    
    def __str__(self):
        return f"Triggers:\n    Air Humidity: {self.ahTrigger} - Soil Humidity: {self.shTrigger} - Temperature: {self.tempTrigger}\nTimers:\n    Telemetry Refresh: {self.refreshTime} - Water Timer: {self.waterTimer}"