package jp.thingy.thingymcconfig_android.fragment;

import android.arch.lifecycle.ViewModelProviders;

import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.databinding.FragmentNetworkselectBinding;
import jp.thingy.thingymcconfig_android.viewmodel.NetworkSelectViewModel;

public class NetworkSelectFragment extends ViewModelFragment<FragmentNetworkselectBinding, NetworkSelectViewModel> {
    @Override
    protected int getLayout() {
        return R.layout.fragment_networkselect;
    }

    @Override
    protected NetworkSelectViewModel instantiateViewModel() {
        return ViewModelProviders.of(getActivity()).get(NetworkSelectViewModel.class);
    }

    public static NetworkSelectFragment newInstance() {
        return new NetworkSelectFragment();
    }
}
