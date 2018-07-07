package jp.thingy.thingymcconfig_android.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import jp.thingy.thingymcconfig.ThingyMcConfig;
import jp.thingy.thingymcconfig.model.StatusResponse;
import jp.thingy.thingymcconfig_android.BR;
import jp.thingy.thingymcconfig_android.ThingyMcConfigApplication;

public class ConfigProgressViewModel extends ViewModel {

    public String ssid, password;

    public final Progress progress = new Progress();
    @Inject
    ThingyMcConfig thingyMcConfig;

    public ConfigProgressViewModel() {
        ThingyMcConfigApplication.getComponent().inject(this);

        Observable.interval(10, TimeUnit.SECONDS)
                .flatMap(i -> Observable.just(thingyMcConfig.status()))
                .subscribeOn(Schedulers.io())
                .subscribe(r -> {

                    if (r.network.config_state == StatusResponse.Network.ConfigState.UNCONFIGURED) {
                        thingyMcConfig.config(ssid, password);
                    }

                    progress.updateFrom(r);
                }, e -> {
                    if (e instanceof IOException) {
                        e.printStackTrace();
                        return;
                    }
                    throw new RuntimeException(e);
                });
    }

    public enum Status {
        PENDING, OK, FAILED
    }

    public static class Progress extends BaseObservable {
        @Bindable
        public Status configuration = Status.PENDING;

        @Bindable
        public Status supplicant = Status.PENDING;

        @Bindable
        public Status dhcp4 = Status.PENDING;

        public void updateFrom(StatusResponse statusResponse) {
            if (statusResponse.network.config_state ==
                    StatusResponse.Network.ConfigState.INPROGRESS)
                configuration = Status.OK;

            if (statusResponse.network.supplicant.connected)
                supplicant = Status.OK;
            if (statusResponse.network.dhcp4.state == StatusResponse.Network.DHCP4.State.CONFIGURED)
                dhcp4 = Status.OK;

            notifyPropertyChanged(BR.configurable);
            notifyPropertyChanged(BR.supplicant);
            notifyPropertyChanged(BR.dhcp4);
        }
    }
}
