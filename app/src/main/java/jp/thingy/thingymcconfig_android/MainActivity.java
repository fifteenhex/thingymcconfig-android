package jp.thingy.thingymcconfig_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import jp.thingy.thingymcconfig.ThingyMcConfig;

public class MainActivity extends AppCompatActivity {

    private final int REQ_PERMISSIONS = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThingyMcConfig thingyMcConfig = new ThingyMcConfig(this);
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};
                requestPermissions(permissions, REQ_PERMISSIONS);
            }
        }

        thingyMcConfig.findThingies();
    }
}
