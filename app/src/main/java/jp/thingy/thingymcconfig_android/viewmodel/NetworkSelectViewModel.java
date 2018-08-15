package jp.thingy.thingymcconfig_android.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.v4.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.subjects.PublishSubject;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig.model.ScanResponse;
import jp.thingy.thingymcconfig_android.adapter.ThingyScanResultAdapter;

public class NetworkSelectViewModel extends RxThingyViewModel
        implements ThingyScanResultAdapter.OnThingyScanResultSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = NetworkSelectViewModel.class.getSimpleName();

    private Disposable scanDisposable;
    private final PublishSubject<Integer> refresh = PublishSubject.create();

    public final PublishSubject<ScanResponse.ThingyScanResult> selectedScanResult =
            PublishSubject.create();
    public ObservableArrayList<ScanResponse.ThingyScanResult> scanResults = new ObservableArrayList<>();
    public ObservableBoolean refreshing = new ObservableBoolean();

    public NetworkSelectViewModel() {
        super();
        scanDisposable =
                thingyMcConfig.listenForEvent(RxThingyMcConfig.Event.SELECTED)
                        .flatMap(e -> Observable.merge(Observable.interval(0, 30, TimeUnit.SECONDS), refresh))
                        .flatMap(i -> thingyMcConfig.rxScan())
                        .flatMap(scanResults -> Observable
                                .fromIterable(scanResults.scanresults))
                        .doOnError(t -> {
                            if (t instanceof IOException) {
                                t.printStackTrace();
                            } else
                                throw Exceptions.propagate(t);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(sr -> {
                            scanResults.add(sr);
                            refreshing.set(false);
                        }, e -> {
                            throw new RuntimeException(e);
                        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        scanDisposable.dispose();
        selectedScanResult.onComplete();
        refresh.onComplete();
    }

    @Override
    public void onThingyScanResultSelected(ScanResponse.ThingyScanResult scanResult) {
        selectedScanResult.onNext(scanResult);
    }

    @Override
    public void onRefresh() {
        refresh.onNext(-2);
    }
}
