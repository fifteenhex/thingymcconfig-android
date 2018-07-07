package jp.thingy.thingymcconfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jp.thingy.thingymcconfig.model.ConfigRequest;
import jp.thingy.thingymcconfig.model.ConfigResponse;
import jp.thingy.thingymcconfig.model.ScanResponse;
import jp.thingy.thingymcconfig.model.StatusResponse;
import jp.thingy.thingymcconfig.model.Thingy;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThingyMcConfig {

    private static final String TAG = ThingyMcConfig.class.getSimpleName();

    private final WifiManager wifiManager;
    private final String ssidPrefix;
    private final ThingyMcConfigService service;
    private boolean wantToBeConnected;
    private Thingy selectedThingy;
    private int networkId;
    private IntentFilter wifiStateChangeIntentFilter =
            new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);

    private BroadcastReceiver wifiStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                Log.d(TAG, String.format("Connected to %s(%s)", wifiInfo.getSSID(),
                        wifiInfo.getBSSID()));
            }
        }
    };

    /**
     * @param context
     * @param ssidPrefix
     */
    public ThingyMcConfig(Context context, String ssidPrefix) {
        wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        this.ssidPrefix = ssidPrefix;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.1:1338")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ThingyMcConfigService.class);

        context.registerReceiver(wifiStateChangeReceiver, wifiStateChangeIntentFilter);
    }

    private String wrapString(String ssid) {
        return String.format("\"%s\"", ssid);
    }

    private boolean isAThingy(ScanResult scanResult) {
        if (ssidPrefix != null && scanResult.SSID.startsWith(ssidPrefix))
            return true;

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

    public boolean connectToThingy(@NonNull Thingy thingy) {
        if (thingy == null)
            throw new IllegalArgumentException("thingy cannot be null");

        selectedThingy = thingy;
        wantToBeConnected = true;

        boolean networkExists = false;
        List<WifiConfiguration> networks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration wc : networks) {
            if (TextUtils.equals(wc.SSID, wrapString(thingy.ssid))) {
                Log.d(TAG, "network configuration already exists");
                networkId = wc.networkId;
                networkExists = true;
                break;
            }
        }

        if (!networkExists) {
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.SSID = wrapString(thingy.ssid);
            wifiConfiguration.preSharedKey = wrapString("reallysecurepassword");
            networkId = wifiManager.addNetwork(wifiConfiguration);
            if (networkId == -1) {
                Log.d(TAG, "failed to add network configuration");
            }
        }

        wifiManager.enableNetwork(networkId, true);

        return false;
    }

    public void disconnectFromThingy() {
        wantToBeConnected = false;
        selectedThingy = null;
        wifiManager.removeNetwork(networkId);
        networkId = -1;
    }

    public List<ScanResponse.ThingyScanResult> scan() throws IOException {
        Response<ScanResponse> response = service.scan().execute();
        return response.body().scanresults;
    }

    public ConfigResponse config(String ssid, String password) throws IOException {
        ConfigRequest configRequest = new ConfigRequest(ssid, password);
        Response<ConfigResponse> response = service.config(configRequest).execute();
        return response.body();
    }

    public StatusResponse status() throws IOException {
        Response<StatusResponse> response = service.status().execute();
        return response.body();
    }
}
