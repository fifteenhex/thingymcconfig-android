package jp.thingy.thingymcconfig_android.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.support.annotation.StringRes;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig.model.StatusResponse;
import jp.thingy.thingymcconfig_android.BR;
import jp.thingy.thingymcconfig_android.R;

public class ConfigProgressViewModel extends RxThingyViewModel {

    public final PublishSubject<Boolean> publishSubject = PublishSubject.create();
    public final ObservableArrayList<ConfigurationTask> tasks = new ObservableArrayList<>();

    public ConfigProgressViewModel() {
        super();

        ConfigurationTask sendconfig = new ConfigurationTask(R.string.configtask_sendconfig);
        ConfigurationTask supplicant = new ConfigurationTask(R.string.configtask_supplicant);
        ConfigurationTask dhcp4 = new ConfigurationTask(R.string.configtask_dhcp4);
        tasks.add(sendconfig);
        tasks.add(supplicant);
        tasks.add(dhcp4);

        registerPublishSubject(publishSubject);
        registerDisposable(thingyMcConfig.listenForEvent(RxThingyMcConfig.Event.CONFIGURING)
                .flatMap(e ->
                        Observable.merge(thingyMcConfig.listenForEvent(RxThingyMcConfig.Event.CONNECTED).flatMap(event -> Observable.just(-1)),
                                Observable.interval(10, TimeUnit.SECONDS))
                )
                .toFlowable(BackpressureStrategy.DROP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    registerDisposable(thingyMcConfig.rxStatus().subscribe(r -> {
                        if (r.network.config_state ==
                                StatusResponse.Network.ConfigState.INPROGRESS)
                            sendconfig.setStatus(ConfigurationTask.Status.OK);

                        if (r.network.supplicant.connected)
                            supplicant.setStatus(ConfigurationTask.Status.OK);

                        if (r.network.dhcp4.state == StatusResponse.Network.DHCP4.State.CONFIGURED)
                            dhcp4.setStatus(ConfigurationTask.Status.OK);
                    }, t -> {
                        handleMaskableExceptions(t);
                    }));
                }, e -> {
                    throw new RuntimeException(e);
                }));
    }

    public void onFinish() {
        publishSubject.onNext(true);
    }

    public static class ConfigurationTask extends BaseObservable {

        @StringRes
        public final int label;

        public ConfigurationTask(@StringRes int label) {
            this.label = label;
        }

        public enum Status {
            PENDING, OK, FAILED
        }

        @Bindable
        public Status status = Status.PENDING;

        public String formatLabel(Context context) {
            return context.getString(label);
        }

        public void setStatus(Status status) {
            this.status = status;
            notifyPropertyChanged(BR.status);
        }
    }

    /*
    public static class Progress extends BaseObservable {
        public void updateFrom(StatusResponse statusResponse) {


            notifyPropertyChanged(BR.configurable);
            notifyPropertyChanged(BR.supplicant);
            notifyPropertyChanged(BR.dhcp4);
        }
    }*/
}
