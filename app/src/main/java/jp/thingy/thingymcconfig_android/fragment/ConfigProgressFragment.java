package jp.thingy.thingymcconfig_android.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;

import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.databinding.FragmentConfigprogressBinding;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigProgressViewModel;

public class ConfigProgressFragment extends
        ViewModelFragment<FragmentConfigprogressBinding, ConfigProgressViewModel> {

    private static final String ARG_SSID = "ssid";
    private static final String ARG_PASSWORD = "password";

    @Override
    protected int getLayout() {
        return R.layout.fragment_configprogress;
    }

    @Override
    protected ConfigProgressViewModel instantiateViewModel() {
        ConfigProgressViewModel viewModel =
                ViewModelProviders.of(getActivity()).get(ConfigProgressViewModel.class);
        viewModel.ssid = getArguments().getString(ARG_SSID);
        viewModel.password = getArguments().getString(ARG_PASSWORD);
        return viewModel;
    }

    public static ConfigProgressFragment newInstance(@NonNull String ssid,
                                                     @NonNull String password) {
        ConfigProgressFragment fragment = new ConfigProgressFragment();

        Bundle args = new Bundle();
        args.putString(ARG_SSID, ssid);
        args.putString(ARG_PASSWORD, password);
        fragment.setArguments(args);

        return fragment;
    }
}
