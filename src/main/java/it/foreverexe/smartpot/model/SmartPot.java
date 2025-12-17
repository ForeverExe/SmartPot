package it.foreverexe.smartpot.model;


import java.util.UUID;

public class SmartPot {

    private UUID uuid;
    private String name;
    private String version;
    private String type;

    private float ahTrigger;
    private float shTrigger;
    private float refreshTimer;

    public float airHumidity;
    public float soilHumidity;
    public float temperature;
    public float waterTimer;
}
