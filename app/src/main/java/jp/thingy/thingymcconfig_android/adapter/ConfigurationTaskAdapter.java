package jp.thingy.thingymcconfig_android.adapter;

import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import jp.thingy.thingymcconfig_android.viewholder.ConfigurationTaskViewHolder;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigProgressViewModel;

public class ConfigurationTaskAdapter extends ObservableArrayListAdapter<ConfigProgressViewModel.ConfigurationTask, ConfigurationTaskViewHolder> {
    protected ConfigurationTaskAdapter(ObservableArrayList<ConfigProgressViewModel.ConfigurationTask> list) {
        super(list);
    }

    @NonNull
    @Override
    public ConfigurationTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ConfigurationTaskViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigurationTaskViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    public static ConfigurationTaskAdapter createAdapter(ObservableArrayList<ConfigProgressViewModel.ConfigurationTask> list) {
        return new ConfigurationTaskAdapter(list);
    }
}
