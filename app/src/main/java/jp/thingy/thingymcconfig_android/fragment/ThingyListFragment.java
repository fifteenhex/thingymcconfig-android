package jp.thingy.thingymcconfig_android.fragment;

import android.arch.lifecycle.ViewModelProviders;

import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.databinding.FragmentThinglistBinding;
import jp.thingy.thingymcconfig_android.viewmodel.ThingyListViewModel;

public class ThingyListFragment extends ViewModelFragment<FragmentThinglistBinding, ThingyListViewModel> {

    public static ThingyListFragment newInstance() {
        return new ThingyListFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_thinglist;
    }

    @Override
    protected ThingyListViewModel instantiateViewModel() {
        return ViewModelProviders.of(getActivity())
                .get(ThingyListViewModel.class);
    }
}
