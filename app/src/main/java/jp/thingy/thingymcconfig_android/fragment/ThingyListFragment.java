package jp.thingy.thingymcconfig_android.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.activity.MainActivity;
import jp.thingy.thingymcconfig_android.databinding.FragmentThinglistBinding;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigurationViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.ThingyListViewModel;

public class ThingyListFragment extends ViewModelFragment<FragmentThinglistBinding, ThingyListViewModel> {

    private ConfigurationViewModel configurationViewModel;

    public static ThingyListFragment newInstance() {
        return new ThingyListFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_thinglist;
    }

    @Override
    protected ThingyListViewModel instantiateViewModel() {
        return ViewModelProviders.of(this.getActivity())
                .get(ThingyListViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configurationViewModel = ViewModelProviders.of(this.getActivity()).get(ConfigurationViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding.setListener(thingy -> {

            ((MainActivity) getActivity()).getThingyMcConfig().connectToThingy(thingy);

            configurationViewModel.thingy = thingy;
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentcontainer, ConfigurationFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }
}
