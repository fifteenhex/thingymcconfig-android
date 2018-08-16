package jp.thingy.thingymcconfig.model;

import java.util.List;

public class ScanResponse {
    public static class ThingyScanResult {
        public String ssid;
        public String bssid;
        public int frequency;
        public int rssi;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ThingyScanResult) {
                return ((ThingyScanResult) obj).bssid.equals(bssid);
            } else
                return false;
        }
    }

    public List<ThingyScanResult> scanresults;
}
