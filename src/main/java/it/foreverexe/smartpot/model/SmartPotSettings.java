package it.foreverexe.smartpot.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.google.gson.Gson;

public class SmartPotSettings {
    //Settings
    private float shTrigger;
    private float tempTrigger;
    private float refreshTimer;
    private float waterTimer;
    private float ahTrigger;

    public SmartPotSettings() {
        this.shTrigger = 50.0f;
        this.ahTrigger = 43.0f;
        this.refreshTimer = 3.0f;
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

    public void setup(String name, java.util.Scanner br){
        System.out.println("Impostazione per " + name + ":");
        System.out.println(this);
        System.out.println("Imposta i valori in ordine, lasciare vuoto lascia il valore precedente:");

        String input="";
        try {
            System.out.print("Soil Humidity Trigger: ");
            input = br.nextLine().trim();
            if(!input.isEmpty()){
                this.shTrigger = Float.parseFloat(input);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.print("Air Humidity Trigger: ");
            input = br.nextLine().trim();
            if(!input.isEmpty()){
                this.ahTrigger = Float.parseFloat(input);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.print("Temperature Trigger: ");
            input = br.nextLine().trim();
            if(!input.isEmpty()){
                this.tempTrigger = Float.parseFloat(input);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.print("Data Refresh Trigger: ");
            input = br.nextLine().trim();
            if(!input.isEmpty()){
                this.refreshTimer = Float.parseFloat(input);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.print("Water Timer: ");
            input = br.nextLine().trim();
            if(!input.isEmpty()){
                this.waterTimer = Float.parseFloat(input);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    //Creates a JSON version of the settings to send to the Broker
    public String toJson(){
        return new Gson().toJson(this);
    }
}
