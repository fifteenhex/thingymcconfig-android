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

import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig.ThingyMcConfig;
import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.ThingyMcConfigApplication;
import jp.thingy.thingymcconfig_android.fragment.ThingyListFragment;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigurationViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.NetworkSelectViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.ProvisioningViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.ThingyListViewModel;

public class MainActivity extends AppCompatActivity {

    private final int REQ_PERMISSIONS = 1;

    @Inject
    RxThingyMcConfig thingyMcConfig;

    private ThingyListViewModel listViewModel;
    private NetworkSelectViewModel networkSelectViewModel;
    private ConfigurationViewModel configurationViewModel;
    private ProvisioningViewModel provisioningViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThingyMcConfigApplication.getComponent().inject(this);

        final ViewModelProvider viewModelProvider = ViewModelProviders.of(this);
        listViewModel = viewModelProvider.get(ThingyListViewModel.class);
        networkSelectViewModel = viewModelProvider.get(NetworkSelectViewModel.class);
        configurationViewModel = viewModelProvider.get(ConfigurationViewModel.class);
        provisioningViewModel = viewModelProvider.get(ProvisioningViewModel.class);

        DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentcontainer, ThingyListFragment.newInstance())
                    .commit();
        }

        networkSelectViewModel.selectedScanResult
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sr -> {
                    configurationViewModel.configuration.setSsid(sr.ssid);
                    getSupportFragmentManager()
                            .popBackStack();
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

    public ThingyMcConfig getThingyMcConfig() {
        return thingyMcConfig;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thingyMcConfig.disconnectFromThingy();
    }
}
