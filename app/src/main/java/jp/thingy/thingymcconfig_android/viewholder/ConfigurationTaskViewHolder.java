package jp.thingy.thingymcconfig_android.viewholder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.databinding.ViewholderConfigurationtaskBinding;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigProgressViewModel;

public class ConfigurationTaskViewHolder extends DataBindingViewHolder<ViewholderConfigurationtaskBinding> {
    protected ConfigurationTaskViewHolder(ViewholderConfigurationtaskBinding binding) {
        super(binding);
    }

    public static ConfigurationTaskViewHolder create(ViewGroup parent) {
        ViewholderConfigurationtaskBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.viewholder_configurationtask, parent, false);
        return new ConfigurationTaskViewHolder(binding);
    }

    public void bind(ConfigProgressViewModel.ConfigurationTask task) {
        binding.setTask(task);
    }
}
