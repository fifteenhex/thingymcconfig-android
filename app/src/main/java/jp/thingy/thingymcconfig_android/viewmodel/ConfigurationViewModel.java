package jp.thingy.thingymcconfig_android.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import io.reactivex.subjects.PublishSubject;
import jp.thingy.thingymcconfig.model.Thingy;
import jp.thingy.thingymcconfig_android.BR;

public class ConfigurationViewModel extends RxThingyViewModel {

    public final PublishSubject<Action> action = PublishSubject.create();
    public final Configuration configuration = new Configuration();

    public ConfigurationViewModel() {
        super();
    }

    public Thingy getThingy() {
        return thingyMcConfig.getSelectedThingy();
    }

    public void selectNetwork() {
        action.onNext(Action.SELECTNETWORK);
    }

    public void onConfigure() {
        action.onNext(Action.CONFIGURE);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        action.onComplete();
    }

    public enum Action {
        SELECTNETWORK, CONFIGURE
    }

    public static class Configuration extends BaseObservable {
        @Bindable
        public boolean hiddenSSID;

        @Bindable
        public String ssid;

        @Bindable
        public String password;

        public boolean isHiddenSSID() {
            return hiddenSSID;
        }

        public void setHiddenSSID(boolean hiddenSSID) {
            this.hiddenSSID = hiddenSSID;
            notifyPropertyChanged(BR.hiddenSSID);
        }

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
            notifyPropertyChanged(BR.ssid);
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
            notifyPropertyChanged(BR.password);
        }

        @Bindable({"ssid", "password"})
        public boolean isConfigurable() {
            return ssid != null && password != null;
        }
    }
}
