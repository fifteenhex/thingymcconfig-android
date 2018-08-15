package jp.thingy.thingymcconfig_android.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.v4.widget.SwipeRefreshLayout;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig.model.Thingy;
import jp.thingy.thingymcconfig_android.adapter.ThingyAdapter;

public class ThingyListViewModel extends RxThingyViewModel
        implements ThingyAdapter.OnThingySelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    public final PublishSubject<Thingy> selectedThingy = PublishSubject.create();
    public final ObservableArrayList<Thingy> thingies = new ObservableArrayList<>();
    public final ObservableBoolean refreshing = new ObservableBoolean();
    private final PublishSubject<Boolean> refresh = PublishSubject.create();
    private final Disposable disposable;

    public ThingyListViewModel() {
        super();
        disposable = Observable.merge(thingyMcConfig.events
                .filter(event -> event == RxThingyMcConfig.Event.SCANRESULTS)
                .flatMap(event -> Observable.just(true)), refresh)
                .flatMap(event -> Observable.fromIterable(thingyMcConfig.findThingies()))
                .toFlowable(BackpressureStrategy.DROP)
                .filter(thingy -> !thingies.contains(thingy))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(thingy -> {
                    refreshing.set(false);
                    thingies.add(thingy);
                });
    }

    @Override
    public void onThingySelected(Thingy thingy) {
        selectedThingy.onNext(thingy);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        selectedThingy.onComplete();
        refresh.onComplete();
        disposable.dispose();
    }

    @Override
    public void onRefresh() {
        refresh.onNext(true);
    }
}
