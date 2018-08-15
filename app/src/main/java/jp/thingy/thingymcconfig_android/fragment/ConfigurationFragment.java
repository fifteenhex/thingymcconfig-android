package jp.thingy.thingymcconfig_android.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.databinding.FragmentConfigurationBinding;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigurationViewModel;

public class ConfigurationFragment extends ViewModelFragment<FragmentConfigurationBinding, ConfigurationViewModel> {

    public interface Listener {
        void selectNetwork();

        void onConfigure();
    }

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding.setListener(new Listener() {
            @Override
            public void selectNetwork() {
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragmentcontainer, NetworkSelectFragment.newInstance())
                        .commit();
            }

            @Override
            public void onConfigure() {
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragmentcontainer, ConfigProgressFragment
                                .newInstance(viewModel.configuration.ssid, viewModel.configuration.password))
                        .commit();
            }
        });
        return view;
    }
}
