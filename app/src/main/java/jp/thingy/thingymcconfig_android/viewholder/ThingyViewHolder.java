package jp.thingy.thingymcconfig_android.viewholder;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import jp.thingy.thingymcconfig.model.Thingy;
import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.databinding.ViewholderThingyBinding;

public class ThingyViewHolder extends RecyclerView.ViewHolder {

    private final ViewholderThingyBinding binding;

    private ThingyViewHolder(ViewholderThingyBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static ThingyViewHolder create(Context context) {
        ViewholderThingyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.viewholder_thingy,
                null,
                false);
        return new ThingyViewHolder(binding);
    }

    public void bind(Thingy thingy) {
        binding.setThingy(thingy);
    }
}
