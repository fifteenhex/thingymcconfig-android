package jp.thingy.jpthingythingymcconfig_androidrx;

import android.content.Context;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import jp.thingy.thingymcconfig.ThingyMcConfig;
import jp.thingy.thingymcconfig.model.ConfigResponse;
import jp.thingy.thingymcconfig.model.ScanResponse;
import jp.thingy.thingymcconfig.model.StatusResponse;
import jp.thingy.thingymcconfig.model.Thingy;

public class RxThingyMcConfig extends ThingyMcConfig {

    public final PublishSubject<Event> events;

    /**
     * @param context
     * @param ssidPrefix
     */
    public RxThingyMcConfig(Context context, String ssidPrefix) {
        this(context, ssidPrefix, new RxCallback());
    }

    private RxThingyMcConfig(Context context, String ssidPrefix, RxCallback callback) {
        super(context, ssidPrefix, callback);
        events = callback.events;
    }

    public enum Event {
        SCANRESULTS, SELECTED, CONNECTED, DISCONNECTED, CONFIGURING, CONFIGURED
    }

    private static class RxCallback implements Callback {
        PublishSubject<Event> events = PublishSubject.create();

        @Override
        public void onConnectedToSelectedThingy() {
            events.onNext(Event.CONNECTED);
        }

        @Override
        public void onScanResults() {
            events.onNext(Event.SCANRESULTS);
        }
    }

    @Override
    public boolean setSelectedThingy(@NonNull Thingy thingy) {
        if (super.setSelectedThingy(thingy)) {
            events.onNext(Event.SELECTED);
            return true;
        } else
            return false;
    }

    public Observable<ScanResponse> rxScan() {
        return Observable.fromCallable(() -> scan())
                .subscribeOn(Schedulers.io());
    }

    public Observable<StatusResponse> rxStatus() {
        return Observable.fromCallable(() -> status())
                .subscribeOn(Schedulers.io());
    }

    public Observable<ConfigResponse> rxConfig(String ssid, String password) {
        events.onNext(Event.CONFIGURING);
        return Observable.fromCallable(() -> config(ssid, password))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Event> listenForEvent(Event type) {
        return events.filter(event -> event == type);
    }
}
