package jp.thingy.thingymcconfig_android.viewholder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import jp.thingy.thingymcconfig.model.Thingy;
import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.adapter.ThingyAdapter;
import jp.thingy.thingymcconfig_android.databinding.ViewholderThingyBinding;

public class ThingyViewHolder extends DataBindingViewHolder<ViewholderThingyBinding> {

    private ThingyViewHolder(ViewholderThingyBinding binding) {
        super(binding);
    }

    public static ThingyViewHolder create(ViewGroup parent) {
        ViewholderThingyBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.viewholder_thingy, parent, false);
        return new ThingyViewHolder(binding);
    }

    public void bind(Thingy thingy, ThingyAdapter.OnThingySelectedListener listener) {
        binding.setThingy(thingy);
        binding.setListener(listener);
    }
}
