package jp.thingy.thingymcconfig_android.adapter;

import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import jp.thingy.thingymcconfig.model.Thingy;
import jp.thingy.thingymcconfig_android.viewholder.ThingyViewHolder;

public class ThingyAdapter extends ObservableArrayListAdapter<Thingy, ThingyViewHolder> {

    private final OnThingySelectedListener listener;

    private ThingyAdapter(ObservableArrayList<Thingy> thingies,
                          OnThingySelectedListener listener) {
        super(thingies);
        this.listener = listener;
    }

    public static ThingyAdapter createAdapter(ObservableArrayList<Thingy> thingies,
                                              OnThingySelectedListener listener) {
        return new ThingyAdapter(thingies, listener);
    }

    @NonNull
    @Override
    public ThingyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ThingyViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ThingyViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    public interface OnThingySelectedListener {
        void onThingySelected(Thingy thingy);
    }
}
