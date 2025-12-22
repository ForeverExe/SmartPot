package it.foreverexe.smartpot.process;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.foreverexe.smartpot.conf.MqttConfigurationParameters;
import it.foreverexe.smartpot.model.SmartPotDescriptor;
import it.foreverexe.smartpot.model.SmartPotSettings;
import it.foreverexe.smartpot.model.SmartPotTelemetry;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;


/**
 * Main processo of the SmartPot service:
 * - Does Discovery at startup
 *  - Creates a list of Pots and appends the SmartPot objects to the list
 * - Enters a loop where it logs the data given by the devices
 * - If prompted, updates the values of a device
 */
public class SmartPotServer {
    static Gson gson = new Gson();
    static HashMap<String, SmartPotDescriptor> PotsList = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Starting SmartPot Service...");

        try{
            MqttClientPersistence persistence = new MemoryPersistence();

            IMqttClient mqttClient = new MqttClient(
                    String.format("tcp://%s:%d", MqttConfigurationParameters.BROKER_ADDRESS, MqttConfigurationParameters.BROKER_PORT),
                    "SmartPotService",
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

            //Discovery Phase
            mqttClient.subscribe(MqttConfigurationParameters.MQTT_BASIC_TOPIC+"/+"+MqttConfigurationParameters.MQTT_INFO_TOPIC, new IMqttMessageListener() {
                //https://github.com/Intelligent-Internet-of-Things-Course/mqtt-playground/blob/master/src/main/java/it/unimore/dipi/iot/mqtt/playground/process/JsonConsumer.java
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Messaggio dal topic "+topic+" arrivato: ");
                    SmartPotDescriptor device = gson.fromJson(new String(message.getPayload()), SmartPotDescriptor.class);
                    System.out.println((device));
                    PotsList.put(device.getUuid(), device);
                }
            });

            System.out.println(PotsList.size());
            for (SmartPotDescriptor i : PotsList.values()) {
                System.out.println(i);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
