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

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.thingy.thingymcconfig.ThingyMcConfig;
import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.ThingyMcConfigApplication;
import jp.thingy.thingymcconfig_android.fragment.ThingyListFragment;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigurationViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.ProvisioningViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.ThingyListViewModel;

public class MainActivity extends AppCompatActivity {

    private final int REQ_PERMISSIONS = 1;

    @Inject
    ThingyMcConfig thingyMcConfig;

    private ThingyListViewModel listViewModel;
    private ConfigurationViewModel configurationViewModel;
    private ProvisioningViewModel provisioningViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThingyMcConfigApplication.getComponent().inject(this);

        final ViewModelProvider viewModelProvider = ViewModelProviders.of(this);
        listViewModel = viewModelProvider.get(ThingyListViewModel.class);
        configurationViewModel = viewModelProvider.get(ConfigurationViewModel.class);
        provisioningViewModel = viewModelProvider.get(ProvisioningViewModel.class);

        DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentcontainer, ThingyListFragment.newInstance())
                    .commit();
        }
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
                .filter(thingy -> !listViewModel.thingies.contains(thingy))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(thingy -> {
                    listViewModel.thingies.add(thingy);
                    //binding.thingies.getAdapter().notifyDataSetChanged();
                });
    }

    public ThingyMcConfig getThingyMcConfig() {
        return thingyMcConfig;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thingyMcConfig.disconnectFromThingy();
    }
}
