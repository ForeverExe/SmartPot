package it.foreverexe.smartpot.model;

/**
 * This class describes a single Smart Pot with its informations and settings.
 * It has methods to update the settings and send them to the MQTT Broker, while the data is fixed here.
 */
public class SmartPotDescriptor {
    //Info
    private String uuid;
    private String name;
    private String version;
    private String type;

    //DataObject
    public SmartPotTelemetry telemetry;
    public SmartPotSettings settings;

    //The constructor wants the base data of the device,
    //while the settings are standard and uploaded afterwards
    public SmartPotDescriptor(String uuid, String type, String version, String name) {
        this.uuid = uuid;
        this.type = type;
        this.version = version;
        this.name = name;

        this.settings = new SmartPotSettings();
        this.telemetry = new SmartPotTelemetry();
    }

    public void setSettings(SmartPotSettings settings){
        this.settings = settings;
    }
    public SmartPotSettings getSettings(){
        return this.settings;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SmartPotDescriptor:" +
                "\nInfo:\nuuid=" + uuid +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
