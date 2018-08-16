package jp.thingy.thingymcconfig_android.viewmodel;

import android.databinding.ObservableArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig.model.Thingy;
import jp.thingy.thingymcconfig_android.adapter.ThingyAdapter;

public class ThingyListViewModel extends RxThingyViewModel
        implements ThingyAdapter.OnThingySelectedListener {

    public final PublishSubject<Thingy> selectedThingy = PublishSubject.create();
    public final ObservableArrayList<Thingy> thingies = new ObservableArrayList<>();

    public ThingyListViewModel() {
        super();
        registerPublishSubject(selectedThingy);
        registerDisposable(Observable.merge(thingyMcConfig.listenForEvent(RxThingyMcConfig.Event.SCANRESULTS), refresh)
                .toFlowable(BackpressureStrategy.DROP)
                .subscribe(r -> {
                    registerDisposable(Observable.fromIterable(thingyMcConfig.findThingies())
                            .filter(thingy -> !thingies.contains(thingy))
                            .doOnSubscribe(c -> {
                                refreshing.set(true);
                            })
                            .doOnTerminate(() -> {
                                refreshing.set(false);
                            })
                            .subscribe(thingy -> {
                                thingies.add(thingy);
                            }));
                }));
    }

    @Override
    public void onThingySelected(Thingy thingy) {
        selectedThingy.onNext(thingy);
    }
}
