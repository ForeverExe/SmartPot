package it.foreverexe.smartpot.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Telemetry class which starts emptry and then only receives data to be printed for the user.
 */
public class SmartPotTelemetry {

    // Telemetry
    private float airHumidity;
    private float soilHumidity;
    private float temperature;
    private transient LocalDateTime time;
    private transient DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final ZoneId zone = ZoneId.of("UTC");

    public SmartPotTelemetry() {
        this.airHumidity = 0;
        this.soilHumidity = 0;
        this.temperature = 0;
        this.time = LocalDateTime.now();
    }

    public float getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(float airHumidity) {this.airHumidity = airHumidity;}

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

    public void setTime(long timestamp) {time = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), zone);}

    public String getTime() { return time == null ? "" : time.format(dateFormat);
}

    @Override
    public String toString() {
        return "SmartPot:\n"+"{" +
                "\n airHumidity=" + airHumidity +
                ",\n soilHumidity=" + soilHumidity +
                ",\n temperature=" + temperature +
                "\n}"+"\nTime: "+ this.getTime();
    }
}
