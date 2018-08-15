package jp.thingy.thingymcconfig_android.viewmodel;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig_android.ThingyMcConfigApplication;

public class RxThingyViewModel extends ViewModel {

    @Inject
    protected RxThingyMcConfig thingyMcConfig;

    protected RxThingyViewModel() {
        ThingyMcConfigApplication.getComponent().inject(this);
    }
}
