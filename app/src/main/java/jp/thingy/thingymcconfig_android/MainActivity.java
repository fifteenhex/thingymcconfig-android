package jp.thingy.thingymcconfig_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.thingy.thingymcconfig.ThingyMcConfig;
import jp.thingy.thingymcconfig.model.Thingy;
import jp.thingy.thingymcconfig_android.adapters.ThingyAdapter;
import jp.thingy.thingymcconfig_android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final int REQ_PERMISSIONS = 1;

    private ActivityMainBinding binding;
    private ThingyMcConfig thingyMcConfig;
    private ArrayList<Thingy> thingies = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thingyMcConfig = new ThingyMcConfig(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.thingies.setAdapter(new ThingyAdapter(thingies));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};
                requestPermissions(permissions, REQ_PERMISSIONS);
            }
        }

        Observable.interval(10, TimeUnit.SECONDS)
                .flatMap(i -> Observable.fromIterable(thingyMcConfig.findThingies()))
                .filter(thingy -> !thingies.contains(thingy))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(thingy -> {
                    thingies.add(thingy);
                    binding.thingies.getAdapter().notifyDataSetChanged();
                });
    }
}
