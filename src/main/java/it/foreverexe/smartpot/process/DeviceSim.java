package it.foreverexe.smartpot.process;

import it.foreverexe.smartpot.conf.MqttConfigurationParameters;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;

import com.google.gson.Gson;

public class DeviceSim {
    public static Gson gson = new Gson();

    public static void main(String[] args) {
        System.out.println("Starting SmartPot Service...");

        try{
            MqttClientPersistence persistence = new MemoryPersistence();

            IMqttClient mqttClient = new MqttClient(
                    String.format("tcp://%s:%d", MqttConfigurationParameters.BROKER_ADDRESS, MqttConfigurationParameters.BROKER_PORT),
                    "SmartPotDummy",
                    persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(MqttConfigurationParameters.MQTT_USERNAME);
            options.setPassword(new String(MqttConfigurationParameters.MQTT_PASSWORD).toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            //Connect to the target broker
            mqttClient.connect(options);
            System.out.println("Connected!");


            System.out.println("Publish phase -  Topic:" +MqttConfigurationParameters.MQTT_BASIC_TOPIC+"/12345678"+MqttConfigurationParameters.MQTT_INFO_TOPIC);
            HashMap dict = new HashMap();
            dict.put("name", "ProtoDummy");
            dict.put("id", "fdalkjhreiu21314253");
            dict.put("version", "0.0.01-preview");
            dict.put("type", "Raspberry Pi Pico 2W");
            String payload = gson.toJson(dict);
            System.out.println(payload.toString());
            mqttClient.publish(MqttConfigurationParameters.MQTT_BASIC_TOPIC+"/12345678"+MqttConfigurationParameters.MQTT_INFO_TOPIC, payload.getBytes(),2, true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
