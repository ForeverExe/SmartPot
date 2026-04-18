package it.foreverexe.smartpot.process;

import it.foreverexe.smartpot.model.SmartPotSettings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.foreverexe.smartpot.conf.MqttConfigurationParameters;
import it.foreverexe.smartpot.model.SmartPotDescriptor;
import it.foreverexe.smartpot.model.SmartPotSettings;
import it.foreverexe.smartpot.model.SmartPotTelemetry;
import it.foreverexe.smartpot.utils.SenMLPack;
import it.foreverexe.smartpot.utils.SenMLRecord;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * Main process of the SmartPot service:
 * - Does Discovery at startup
 * - Creates a list of Pots and appends the SmartPot objects to the list
 * - Enters a loop where it logs the data given by the devices
 * - If prompted, updates the values of a device
 */
public class SmartPotServer {
    static Gson gson = new Gson();
    //static HashMap<String, SmartPotDescriptor> PotsList = new HashMap<>();
    //static java.util.Map<String, SmartPotDescriptor> PotsList = new java.util.LinkedHashMap<>();
    static Map<String, SmartPotDescriptor> PotsList = Collections.synchronizedMap(new LinkedHashMap<>());
    static boolean running = true;
    static int choice;
    static String deviceKey;

    static String infoTopic = MqttConfigurationParameters.MQTT_BASIC_TOPIC+"/+"+MqttConfigurationParameters.MQTT_INFO_TOPIC;
    static String telTopic = MqttConfigurationParameters.MQTT_BASIC_TOPIC + "/+" + MqttConfigurationParameters.MQTT_TELEMETRY_BASIC_TOPIC;
    public static void main(String[] args) {
        System.out.println("Starting SmartPot Service...");
        Scanner br = new Scanner(System.in);
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
            options.setConnectionTimeout(1000);

            //Connect to the target broker
            mqttClient.connect(options);
            System.out.println("Connected!");

            //Discovery Phase
            System.out.println("Subscribing to Topic:"+ infoTopic);
            //MqttConfigurationParameters.MQTT_BASIC_TOPIC+"/+"+MqttConfigurationParameters.MQTT_INFO_TOPIC
            mqttClient.subscribe(infoTopic, 1, new IMqttMessageListener() {
                //https://github.com/Intelligent-Internet-of-Things-Course/mqtt-playground/blob/master/src/main/java/it/unimore/dipi/iot/mqtt/playground/process/JsonConsumer.java
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    try{
                        //System.out.println("Messaggio dal topic "+topic+" arrivato: ");
                        //System.out.println(message);
                        String jsonPayload = new String(message.getPayload(), StandardCharsets.UTF_8);
                        //System.out.println(jsonPayload);
                        SmartPotDescriptor device = gson.fromJson(jsonPayload, SmartPotDescriptor.class);
                        if (device.getSettings() == null) {
                            device.setSettings(new SmartPotSettings());
                        }
                        if (device.getTelemetry() == null) {
                            device.setTelemetry(new SmartPotTelemetry());
                        }
                        //System.out.println(device.toString());
                        if (device.getName() != null) {
                            PotsList.put(device.getUuid(), device);
                        } else {
                            System.err.println("Errore: Il nome del device è null.");
                        }
                    } catch (com.google.gson.JsonSyntaxException e) {
                        System.err.println("Errore nel parsing del JSON: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Errore imprevisto durante la gestione del messaggio: " + e.getMessage());
                    }
                }
            });


            //Iscriviti a tutte le telemetrie dei dispositivi, poi si occupa di segnare nel dispositivo corretto i vari dati
            System.out.println("Subscribing to Topic:"+ telTopic);
            mqttClient.subscribe(telTopic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String[] topicData = topic.split("/");
                    String uuid = topicData[5];
                    String topicString = topicData[6];
                    System.out.println(topicString);
                    System.out.println(uuid);

                    SmartPotTelemetry potTel = PotsList.get(uuid).getTelemetry();
                    var payload = new String(message.getPayload());

                    SenMLPack load = gson.fromJson(payload, SenMLPack.class);
                    for (SenMLRecord record : load){
                        System.out.println(record);
                        switch (record.getN()){
                            case "air_hum":
                                potTel.setAirHumidity((Float) record.getV());
                                break;
                            case "soil_hum":
                                potTel.setSoilHumidity((Float) record.getV());
                                break;
                            case "temperature":
                                potTel.setTemperature((Float) record.getV());
                                break;
                            case "water":
                                potTel.setWaterUsed((Float) record.getV());
                        }
                    }
                    PotsList.get(uuid).setTelemetry(potTel);
                }
            });


            /*
            for (SmartPotDescriptor i : PotsList.values()) {
                System.out.println(i);
            }
            */
            //System.out.println(PotsList.size());

            Thread.sleep(1000);
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
                        String topic = MqttConfigurationParameters.MQTT_BASIC_TOPIC+"/"+PotsList.get(deviceKey).getUuid()+MqttConfigurationParameters.MQTT_SETTINGS_TOPIC;
                        MqttMessage msg = new MqttMessage(PotsList.get(deviceKey).getSettings().toJson().getBytes());
                        msg.setQos(1);
                        msg.setRetained(true);
                        mqttClient.publish(topic, msg);
                        break;
                    case 1:
                        System.out.println("Seleziona il device di cui visualizzare i dettagli ('q' per annullare):");
                        deviceKey = selectDevice(br);
                        System.out.println(deviceKey);
                        if (deviceKey == null) {
                            break;
                        }
                        System.out.println(PotsList.get(deviceKey).getTelemetry());
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
            for (Map.Entry<String, SmartPotDescriptor> entry : PotsList.entrySet()) { //itera la mappa e stampa i nomi
                System.out.printf("%d) %s - UUID: %s\n", i++, entry.getValue().getName(), entry.getValue().getUuid());
            }
        }

        /**
         * Ask the user to choose a device by index. Returns the device key (name) or null on cancel/invalid.
         */
        private static String selectDevice(Scanner br) {
            listDevices();
            System.out.print("Seleziona: ");
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
                String uuid = new ArrayList<>(PotsList.keySet()).get(idx);
                //System.out.println(uuid);
                return uuid;
            } catch (NumberFormatException ex) {
                System.out.println("Inserire un numero valido.");
                return null;
            }
        }
}
