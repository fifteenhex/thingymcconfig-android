package jp.thingy.thingymcconfig_android.fragment;

import android.arch.lifecycle.ViewModelProviders;

import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.databinding.FragmentConfigprogressBinding;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigProgressViewModel;

public class ConfigProgressFragment extends ViewModelFragment<FragmentConfigprogressBinding, ConfigProgressViewModel> {
    @Override
    protected int getLayout() {
        return R.layout.fragment_configprogress;
    }

    @Override
    protected ConfigProgressViewModel instantiateViewModel() {
        return ViewModelProviders.of(getActivity()).get(ConfigProgressViewModel.class);
    }

    public static ConfigProgressFragment newInstance() {
        ConfigProgressFragment fragment = new ConfigProgressFragment();
        return fragment;
    }
}
