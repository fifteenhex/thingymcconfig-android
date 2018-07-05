package jp.thingy.thingymcconfig_android.viewmodel;

import android.arch.lifecycle.ViewModel;

import jp.thingy.thingymcconfig.model.Thingy;

public class ConfigurationViewModel extends ViewModel {

    public Thingy thingy;
    public String ssid;
    public String password;

    public boolean canConfigure() {
        return ssid != null && password != null;
    }
}
