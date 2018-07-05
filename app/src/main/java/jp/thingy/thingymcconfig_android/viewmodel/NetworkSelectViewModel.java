package jp.thingy.thingymcconfig_android.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.thingy.thingymcconfig.ThingyMcConfig;
import jp.thingy.thingymcconfig.model.ScanResponse;
import jp.thingy.thingymcconfig_android.ThingyMcConfigApplication;

public class NetworkSelectViewModel extends ViewModel {

    private static final String TAG = NetworkSelectViewModel.class.getSimpleName();

    @Inject
    ThingyMcConfig thingyMcConfig;

    private Disposable scanDisposable;

    public ObservableArrayList<ScanResponse.ThingyScanResult> scanResults = new ObservableArrayList<>();

    public NetworkSelectViewModel() {
        ThingyMcConfigApplication.getComponent().inject(this);
        scanDisposable = Observable.fromCallable(() -> thingyMcConfig.scan())
                .flatMap(scanResults -> Observable.fromIterable(scanResults))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sr -> {
                    scanResults.add(sr);
                }, e -> {
                    throw new RuntimeException(e);
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        scanDisposable.dispose();
    }
}
