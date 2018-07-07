package jp.thingy.thingymcconfig_android.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import javax.inject.Inject;

import jp.thingy.thingymcconfig.ThingyMcConfig;
import jp.thingy.thingymcconfig.model.Thingy;
import jp.thingy.thingymcconfig_android.BR;
import jp.thingy.thingymcconfig_android.ThingyMcConfigApplication;

public class ConfigurationViewModel extends ViewModel {

    public final Configuration configuration = new Configuration();
    public Thingy thingy;

    @Inject
    public ThingyMcConfig thingyMcConfig;

    public ConfigurationViewModel() {
        ThingyMcConfigApplication.getComponent().inject(this);
    }

    public static class Configuration extends BaseObservable {
        @Bindable
        public boolean hiddenSSID;

        @Bindable
        public String ssid;

        @Bindable
        public String password;

        public void setHiddenSSID(boolean hiddenSSID) {
            this.hiddenSSID = hiddenSSID;
            notifyPropertyChanged(BR.hiddenSSID);
        }

        public boolean isHiddenSSID() {
            return hiddenSSID;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
            notifyPropertyChanged(BR.ssid);
        }

        public String getSsid() {
            return ssid;
        }

        public void setPassword(String password) {
            this.password = password;
            notifyPropertyChanged(BR.password);
        }

        public String getPassword() {
            return password;
        }

        @Bindable({"ssid", "password"})
        public boolean isConfigurable() {
            return ssid != null && password != null;
        }
    }
}
