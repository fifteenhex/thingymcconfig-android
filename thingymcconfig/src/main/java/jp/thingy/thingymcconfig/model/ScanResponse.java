package jp.thingy.thingymcconfig.model;

import java.util.List;

public class ScanResponse {
    public static class ThingyScanResult {
        public String ssid;
        public String bssid;
        public int frequency;
        public int rssi;
    }

    public List<ThingyScanResult> scanresults;
}
