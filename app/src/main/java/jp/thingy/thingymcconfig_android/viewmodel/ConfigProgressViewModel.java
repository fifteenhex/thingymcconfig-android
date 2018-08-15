package jp.thingy.thingymcconfig_android.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig.model.StatusResponse;
import jp.thingy.thingymcconfig_android.BR;

public class ConfigProgressViewModel extends RxThingyViewModel {

    public final PublishSubject<Boolean> publishSubject = PublishSubject.create();
    public final Progress progress = new Progress();

    public ConfigProgressViewModel() {
        super();
        thingyMcConfig.listenForEvent(RxThingyMcConfig.Event.CONFIGURING)
                .flatMap(e ->
                        Observable.merge(thingyMcConfig.listenForEvent(RxThingyMcConfig.Event.CONNECTED).flatMap(event -> Observable.just(-1)),
                                Observable.interval(10, TimeUnit.SECONDS)).flatMap(i -> thingyMcConfig.rxStatus())
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> {
                    progress.updateFrom(r);
                }, e -> {
                    throw new RuntimeException(e);
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        publishSubject.onComplete();
    }

    public void onFinish() {
        publishSubject.onNext(true);
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
