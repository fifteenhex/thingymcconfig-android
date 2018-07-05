package jp.thingy.thingymcconfig_android.fragment;

import android.arch.lifecycle.ViewModel;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.thingy.thingymcconfig_android.BR;

public abstract class ViewModelFragment<DB extends ViewDataBinding, VM extends ViewModel> extends Fragment {

    protected DB binding;
    protected VM viewModel;

    abstract protected int getLayout();

    abstract protected VM instantiateViewModel();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = instantiateViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                getLayout(), container, false);
        binding.setVariable(BR.viewModel, viewModel);
        return binding.getRoot();
    }
}
