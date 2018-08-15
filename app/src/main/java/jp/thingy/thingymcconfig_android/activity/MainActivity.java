package jp.thingy.thingymcconfig_android.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.ThingyMcConfigApplication;
import jp.thingy.thingymcconfig_android.fragment.ConfigProgressFragment;
import jp.thingy.thingymcconfig_android.fragment.ConfigurationFragment;
import jp.thingy.thingymcconfig_android.fragment.NetworkSelectFragment;
import jp.thingy.thingymcconfig_android.fragment.ThingyListFragment;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigProgressViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigurationViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.NetworkSelectViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.ThingyListViewModel;

public class MainActivity extends AppCompatActivity {

    private final int REQ_PERMISSIONS = 1;

    @Inject
    RxThingyMcConfig thingyMcConfig;

    private ThingyListViewModel listViewModel;
    private NetworkSelectViewModel networkSelectViewModel;
    private ConfigurationViewModel configurationViewModel;
    private ConfigProgressViewModel configProgressViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThingyMcConfigApplication.getComponent().inject(this);

        final ViewModelProvider viewModelProvider = ViewModelProviders.of(this);
        listViewModel = viewModelProvider.get(ThingyListViewModel.class);
        networkSelectViewModel = viewModelProvider.get(NetworkSelectViewModel.class);
        configurationViewModel = viewModelProvider.get(ConfigurationViewModel.class);
        configProgressViewModel = viewModelProvider.get(ConfigProgressViewModel.class);

        DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentcontainer, ThingyListFragment.newInstance())
                    .commit();
        }

        listViewModel.selectedThingy
                .flatMap(thingy -> Observable.just(thingyMcConfig.setSelectedThingy(thingy)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> {
                    if (r) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentcontainer, ConfigurationFragment.newInstance())
                                .addToBackStack(null)
                                .commit();
                    }
                });

        configurationViewModel.action
                /*.flatMap(a -> {
                    if (a == ConfigurationViewModel.Action.CONFIGURE)
                        thingyMcConfig.config(configurationViewModel.configuration.ssid,
                                configurationViewModel.configuration.password);
                    return Observable.just(a);
                })*/
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> {
                    switch (a) {
                        case SELECTNETWORK:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.fragmentcontainer, NetworkSelectFragment.newInstance())
                                    .commit();
                            break;
                        case CONFIGURE:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.fragmentcontainer, ConfigProgressFragment
                                            .newInstance())
                                    .commit();
                            break;
                    }
                });

        networkSelectViewModel.selectedScanResult
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sr -> {
                    configurationViewModel.configuration.setSsid(sr.ssid);
                    getSupportFragmentManager()
                            .popBackStack();
                });

        configProgressViewModel.publishSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentcontainer, ThingyListFragment.newInstance())
                            .commit();
                });
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thingyMcConfig.disconnectFromThingy();
    }
}
