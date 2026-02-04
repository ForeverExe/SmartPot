package it.foreverexe.smartpot.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SmartPotSettings {
    //Settings
    private float shTrigger;
    private float tempTrigger;
    private float refreshTimer;
    private float waterTimer;
    private float ahTrigger;

    private boolean setting = false;

    public SmartPotSettings() {
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
        return "SmartPotSettings\n" +
                "Soil Humidity Trigger=" + shTrigger +
                "%, Temperature Trigger=" + tempTrigger +
                "C, Refresh Timer=" + refreshTimer +
                "s, Water Timer=" + waterTimer +
                "s, Air Humidity Trigger=" + ahTrigger +
                "% \n";
    }

    public void setup(String name){
        Scanner br = new Scanner(System.in);
        System.out.println("Impostazione per " + name + ":");
        System.out.println(this.toString());
        System.out.println("Imposta i valori in ordine, lasciare vuoto lascia il valore di default:");
        try {
            this.shTrigger = br.nextFloat();
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        try {
            this.ahTrigger = br.nextFloat();
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        try {
            this.tempTrigger = br.nextFloat();
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        try {
            this.refreshTimer = br.nextInt();
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        try {
            this.waterTimer = br.nextFloat();
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
