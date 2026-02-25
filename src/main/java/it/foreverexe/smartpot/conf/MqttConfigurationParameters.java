package it.foreverexe.smartpot.conf;

public class MqttConfigurationParameters {
  public static String BROKER_ADDRESS = "155.185.4.4";
  public static int BROKER_PORT = 7883;
  public static final String MQTT_USERNAME = "324749@studenti.unimore.it";
  public static final String MQTT_PASSWORD = "xdkjltekfseyslaq";
  public static final String MQTT_BASIC_TOPIC = String.format("/iot/user/%s/d", MQTT_USERNAME);
  public static final String MQTT_INFO_TOPIC = "/i";
  public static final String MQTT_SETTINGS_TOPIC = "/s";
  public static final String MQTT_TELEMETRY_BASIC_TOPIC = "/t";
  public static final String MQTT_TELEMETRY_SOILHUM = "/sh";
  public static final String MQTT_TELEMETRY_AIRHUM = "/ah";
  public static final String MQTT_TELEMETRY_TEMP = "/temp";
  public static final String MQTT_TELEMETRY_WATER = "/water";
}