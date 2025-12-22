package it.foreverexe.smartpot.model;

public class SmartPotSettings {
    //Settings
    private float shTrigger;
    private float tempTrigger;
    private float refreshTimer;
    private float waterTimer;
    private float ahTrigger;

    SmartPotSettings() {
        this.shTrigger = 50.0f;
        this.ahTrigger = 43.0f;
        this.refreshTimer = 3;
        this.waterTimer = 2.0f;
        this.tempTrigger = 50.0f;
    }

    public float getAhTrigger() {
        return ahTrigger;
    }

    public void setAhTrigger(float ahTrigger) {
        this.ahTrigger = ahTrigger;
    }

    public float getShTrigger() {
        return shTrigger;
    }

    public void setShTrigger(float shTrigger) {
        this.shTrigger = shTrigger;
    }

    public float getTempTrigger() {
        return tempTrigger;
    }

    public void setTempTrigger(float tempTrigger) {
        this.tempTrigger = tempTrigger;
    }

    public float getRefreshTimer() {
        return refreshTimer;
    }

    public void setRefreshTimer(float refreshTimer) {
        this.refreshTimer = refreshTimer;
    }

    public float getWaterTimer() {
        return waterTimer;
    }

    public void setWaterTimer(float waterTimer) {
        this.waterTimer = waterTimer;
    }

    @Override
    public String toString() {
        return "SmartPotSettings{" +
                "shTrigger=" + shTrigger +
                ", tempTrigger=" + tempTrigger +
                ", refreshTimer=" + refreshTimer +
                ", waterTimer=" + waterTimer +
                ", ahTrigger=" + ahTrigger +
                '}';
    }
}
