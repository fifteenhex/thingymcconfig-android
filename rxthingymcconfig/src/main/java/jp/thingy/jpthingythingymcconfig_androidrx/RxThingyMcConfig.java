package jp.thingy.jpthingythingymcconfig_androidrx;

import android.content.Context;

import io.reactivex.subjects.PublishSubject;
import jp.thingy.thingymcconfig.ThingyMcConfig;

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
        SCANRESULTS, CONNECTED, DISCONNECTED
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
}
