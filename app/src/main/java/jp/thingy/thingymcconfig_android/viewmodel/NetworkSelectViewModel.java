package jp.thingy.thingymcconfig_android.viewmodel;

import android.databinding.ObservableArrayList;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig.model.ScanResponse;
import jp.thingy.thingymcconfig_android.adapter.ThingyScanResultAdapter;

public class NetworkSelectViewModel extends RxThingyViewModel
        implements ThingyScanResultAdapter.OnThingyScanResultSelectedListener {

    private static final String TAG = NetworkSelectViewModel.class.getSimpleName();

    private Disposable scanDisposable;

    public final PublishSubject<ScanResponse.ThingyScanResult> selectedScanResult =
            PublishSubject.create();
    public ObservableArrayList<ScanResponse.ThingyScanResult> scanResults = new ObservableArrayList<>();

    public NetworkSelectViewModel() {
        super();
        scanDisposable = Observable.merge(thingyMcConfig.events
                        .filter(event -> event == RxThingyMcConfig.Event.CONNECTED)
                        .flatMap(e -> Observable.just(-1)),
                Observable.interval(0, 30, TimeUnit.SECONDS))
                .flatMap(i -> Observable.fromCallable(() -> thingyMcConfig.scan()))
                .flatMap(scanResults -> Observable.fromIterable(scanResults))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sr -> {
                    scanResults.add(sr);
                }, e -> {
                    e.printStackTrace();
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        scanDisposable.dispose();
        selectedScanResult.onComplete();
    }

    @Override
    public void onThingyScanResultSelected(ScanResponse.ThingyScanResult scanResult) {
        selectedScanResult.onNext(scanResult);
    }
}
