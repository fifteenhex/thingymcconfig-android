package jp.thingy.thingymcconfig_android.viewmodel;

import android.databinding.ObservableArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig.model.Thingy;

public class ThingyListViewModel extends RxThingyViewModel {

    public ObservableArrayList<Thingy> thingies = new ObservableArrayList<>();

    public ThingyListViewModel() {
        super();
        thingyMcConfig.events
                .filter(event -> event == RxThingyMcConfig.Event.SCANRESULTS)
                .flatMap(event -> Observable.fromIterable(thingyMcConfig.findThingies()))
                .filter(thingy -> !thingies.contains(thingy))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(thingy -> {
                    thingies.add(thingy);
                });
    }
}
