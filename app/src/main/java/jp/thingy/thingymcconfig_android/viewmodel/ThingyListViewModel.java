package jp.thingy.thingymcconfig_android.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;

import jp.thingy.thingymcconfig.model.Thingy;

public class ThingyListViewModel extends ViewModel {
    public ObservableArrayList<Thingy> thingies = new ObservableArrayList<>();
}
