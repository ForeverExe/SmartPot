package it.foreverexe.smartpot.model;

/**
 * Telemetry class which starts emptry and then only receives data to be printed for the user.
 */
public class SmartPotTelemetry {

    // Telemetry
    private float airHumidity;
    private float soilHumidity;
    private float temperature;
    private float waterUsed;

    public SmartPotTelemetry() {
        this.airHumidity = 0;
        this.soilHumidity = 0;
        this.temperature = 0;
        this.waterUsed = 0;
    }

    public float getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(float airHumidity) {
        this.airHumidity = airHumidity;
    }

    public float getSoilHumidity() {
        return soilHumidity;
    }

    public void setSoilHumidity(float soilHumidity) {
        this.soilHumidity = soilHumidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getWaterUsed() {
        return waterUsed;
    }

    public void setWaterUsed(float waterUsed) {
        this.waterUsed = waterUsed;
    }

    @Override
    public String toString() {
        return "SmartPot{" +
                " airHumidity=" + airHumidity +
                ", soilHumidity=" + soilHumidity +
                ", temperature=" + temperature +
                ", waterUsed=" + waterUsed +
                '}';
    }
}
