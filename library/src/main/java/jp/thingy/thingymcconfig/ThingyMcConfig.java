package jp.thingy.thingymcconfig;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jp.thingy.thingymcconfig.model.Thingy;

public class ThingyMcConfig {

    private WifiManager wifiManager;

    public ThingyMcConfig(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    private boolean isAThingy(ScanResult scanResult) {
        Field ieArrayField;
        try {
            ieArrayField = ScanResult.class.getDeclaredField("informationElements");
        } catch (NoSuchFieldException nsfe) {
            throw new RuntimeException(nsfe);
        }

        try {
            Object ies = ieArrayField.get(scanResult);
            for (int i = 0; i < Array.getLength(ies); i++) {
                try {
                    Object ie = Array.get(ies, i);
                    Field ieIdField = ie.getClass().getDeclaredField("id");
                    Field ieBytesField = ie.getClass().getDeclaredField("bytes");

                    int id = ieIdField.getInt(ie);
                    if (id != 0xDD)
                        continue;

                    byte[] bytes = (byte[]) ieBytesField.get(ie);

                    String field = new String(bytes);
                    if (field.startsWith("thingymcconfig"))
                        return true;
                } catch (NoSuchFieldException nsfe) {
                    throw new RuntimeException(nsfe);
                }
            }
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }

        return false;
    }

    public List<Thingy> findThingies() {
        ArrayList<Thingy> thingies = new ArrayList<>();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        for (ScanResult sr : scanResults) {
            if (isAThingy(sr)) {
                thingies.add(Thingy.from(sr));
            }
        }
        return thingies;
    }
}
