package jp.thingy.thingymcconfig_android.fragment;

import android.arch.lifecycle.ViewModelProviders;

import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.databinding.FragmentConfigurationBinding;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigurationViewModel;

public class ConfigurationFragment extends ViewModelFragment<FragmentConfigurationBinding, ConfigurationViewModel> {

    public static ConfigurationFragment newInstance() {
        return new ConfigurationFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_configuration;
    }

    @Override
    protected ConfigurationViewModel instantiateViewModel() {
        return ViewModelProviders.of(getActivity()).get(ConfigurationViewModel.class);
    }
}
