package jp.thingy.thingymcconfig.model;

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;

public class Thingy {

    public final String ssid;
    public final String bssid;

    public Thingy(@NonNull String ssid, @NonNull String bssid) {
        this.ssid = ssid;
        this.bssid = bssid;
    }

    public static Thingy from(ScanResult scanResult) {
        return new Thingy(scanResult.SSID, scanResult.BSSID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Thingy)
            return ((Thingy) obj).bssid.equals(bssid);
        return false;
    }
}
