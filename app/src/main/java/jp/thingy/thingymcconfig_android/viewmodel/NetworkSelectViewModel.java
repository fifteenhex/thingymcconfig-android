package jp.thingy.thingymcconfig_android.viewmodel;

import android.databinding.ObservableArrayList;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig.model.ScanResponse;
import jp.thingy.thingymcconfig_android.adapter.ThingyScanResultAdapter;

public class NetworkSelectViewModel extends RxThingyViewModel
        implements ThingyScanResultAdapter.OnThingyScanResultSelectedListener {

    public final PublishSubject<ScanResponse.ThingyScanResult> selectedScanResult =
            PublishSubject.create();
    public ObservableArrayList<ScanResponse.ThingyScanResult> scanResults = new ObservableArrayList<>();

    public NetworkSelectViewModel() {
        super();
        registerPublishSubject(selectedScanResult);
        registerPublishSubject(refresh);

        registerDisposable(thingyMcConfig.listenForEvent(RxThingyMcConfig.Event.SELECTED)
                .flatMap(e -> Observable.merge(Observable.interval(0, 30,
                        TimeUnit.SECONDS), refresh))
                .toFlowable(BackpressureStrategy.DROP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    registerDisposable(thingyMcConfig.rxScan()
                            .flatMap(scanResults -> Observable
                                    .fromIterable(scanResults.scanresults))
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(c -> {
                                refreshing.set(true);
                            })
                            .doOnTerminate(() -> {
                                refreshing.set(false);
                            })
                            .subscribe(sr -> {
                                if (!scanResults.contains(sr))
                                    scanResults.add(sr);
                            }, t -> {
                                handleMaskableExceptions(t);
                            }));
                }));
    }

    @Override
    public void onThingyScanResultSelected(ScanResponse.ThingyScanResult scanResult) {
        selectedScanResult.onNext(scanResult);
    }
}
