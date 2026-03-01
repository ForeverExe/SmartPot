package it.foreverexe.smartpot.process;

import it.foreverexe.smartpot.model.SmartPotSettings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.foreverexe.smartpot.conf.MqttConfigurationParameters;
import it.foreverexe.smartpot.model.SmartPotDescriptor;
import it.foreverexe.smartpot.model.SmartPotSettings;
import it.foreverexe.smartpot.model.SmartPotTelemetry;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Main process of the SmartPot service:
 * - Does Discovery at startup
 * - Creates a list of Pots and appends the SmartPot objects to the list
 * - Enters a loop where it logs the data given by the devices
 * - If prompted, updates the values of a device
 */
public class SmartPotServer {
    static Gson gson = new Gson();
    // use a LinkedHashMap so iteration order is predictable (discovery order)
    static java.util.Map<String, SmartPotDescriptor> PotsList = new java.util.LinkedHashMap<>();

    static boolean running = true;
    static int choice;
    static String deviceKey;
    static IMqttClient mqttClient;

    public static void main(String[] args) {
        System.out.println("Starting SmartPot Service...");
        String infoTopic = MqttConfigurationParameters.MQTT_BASIC_TOPIC+"/+"+MqttConfigurationParameters.MQTT_INFO_TOPIC;
        Scanner br = new Scanner(System.in);
        try{
            MqttClientPersistence persistence = new MemoryPersistence();

            mqttClient = new MqttClient(
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


            System.out.println("Discovery phase -  Topic:" +infoTopic);
            mqttClient.subscribe(infoTopic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Messaggio dal topic " + topic + " arrivato: ");
                    SmartPotDescriptor device = gson.fromJson(new String(message.getPayload()), SmartPotDescriptor.class);
                    System.out.println((device));
                    PotsList.put(device.getName(), device);
                }
            });

            // subscribe to the Last Will topic so we can remove offline devices
            mqttClient.subscribe(MqttConfigurationParameters.MQTT_BASIC_TOPIC+"/+/lwt", new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception{
                    // topic format: /iot/user/.../d/{uuid}/lwt
                    String[] tokens = topic.split("/");
                    if (tokens.length >= 2) {
                        String uuid = tokens[tokens.length-2];
                        // find descriptor containing this uuid and remove it
                        String keyToRemove = null;
                        for (var e : PotsList.entrySet()) {
                            if (e.getValue().getUuid().equals(uuid)) {
                                keyToRemove = e.getKey();
                                break;
                            }
                        }
                        if (keyToRemove != null) {
                            PotsList.remove(keyToRemove);
                            System.out.println("Device disconnected and removed: " + keyToRemove + " (" + uuid + ")");
                        } else {
                            System.out.println("Unknown device gone offline (uuid=" + uuid + ")");
                        }
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try{
            Thread.sleep(2000);
            while(running){
                System.out.println("\nSeleziona una delle opzioni:");
                System.out.println("0. Invia nuove impostazioni al device \n1. Leggi telemetria del device \n2. Elenco dei device connessi \n3. Esci");
                System.out.print("Seleziona una opzione: ");
                var line = br.nextLine();
                try {
                    choice = Integer.parseInt(line);
                } catch (NumberFormatException nfe) {
                    choice = -1;
                }

                switch (choice){
                    case 0:
                        // set new settings on a selected device
                        System.out.println("Seleziona il device per inviare impostazioni ('q' per annullare):");
                        deviceKey = selectDevice(br);
                        if (deviceKey == null) {
                            break;
                        }
                        SmartPotSettings settings = new SmartPotSettings();
                        settings.setup(PotsList.get(deviceKey).getName());
                        PotsList.get(deviceKey).setSettings(settings);
                        System.out.println("Impostazioni inserite:\n" + PotsList.get(deviceKey).getSettings());
                        String topic = MqttConfigurationParameters.MQTT_BASIC_TOPIC+"/"+PotsList.get(deviceKey).getUuid()+"/"+MqttConfigurationParameters.MQTT_SETTINGS_TOPIC;
                        MqttMessage msg = new MqttMessage(PotsList.get(deviceKey).getSettings().toJson().getBytes());
                        msg.setQos(1);
                        msg.setRetained(true);
                        mqttClient.publish(topic, msg);
                        break;
                    case 1:
                        System.out.println("Seleziona il device di cui visualizzare i dettagli ('q' per annullare):");
                        deviceKey = selectDevice(br);
                        if (deviceKey == null) {
                            break;
                        }
                        System.out.println(PotsList.get(deviceKey));
                        break;
                    case 2:
                        listDevices();
                        break;
                    case 3:
                        System.out.println("Spegnendo...");
                        mqttClient.disconnect();
                        mqttClient.close();
                        running = false;
                        break;
                    default:
                        System.out.println("Opzione non valida.");
                        break;
                }
             System.out.println("\n");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Print all discovered devices with an index.
     */
    private static void listDevices() {
        if (PotsList.isEmpty()) {
            System.out.println("Nessun device disponibile.");
            return;
        }
        int i = 0;
        for (var name : PotsList.keySet()) {
            System.out.printf("%d) %s\n", i++, name);
        }
    }

    /**
     * Ask the user to choose a device by index. Returns the device key (name) or null on cancel/invalid.
     */
    private static String selectDevice(Scanner br) {
        listDevices();
        System.out.print("Indice: ");
        String sel = br.nextLine();
        if (sel.equalsIgnoreCase("q")) {
            return null;
        }
        try {
            int idx = Integer.parseInt(sel);
            if (idx < 0 || idx >= PotsList.size()) {
                System.out.println("Indice non valido.");
                return null;
            }
            return new java.util.ArrayList<>(PotsList.keySet()).get(idx);
        } catch (NumberFormatException ex) {
            System.out.println("Inserire un numero valido.");
            return null;
        }
    }

}
