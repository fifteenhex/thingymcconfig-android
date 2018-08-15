package jp.thingy.thingymcconfig.model;

public class ConfigRequest {

    public final String ssid;
    public final String psk;

    public ConfigRequest(String ssid, String psk) {
        this.ssid = ssid;
        this.psk = psk;
    }
}
