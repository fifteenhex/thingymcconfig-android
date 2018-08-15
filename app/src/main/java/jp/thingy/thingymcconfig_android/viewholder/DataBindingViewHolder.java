package jp.thingy.thingymcconfig_android.viewholder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public abstract class DataBindingViewHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected DB binding;

    protected DataBindingViewHolder(DB binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
