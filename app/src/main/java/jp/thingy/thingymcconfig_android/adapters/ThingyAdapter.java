package jp.thingy.thingymcconfig_android.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import jp.thingy.thingymcconfig.model.Thingy;
import jp.thingy.thingymcconfig_android.viewholder.ThingyViewHolder;

public class ThingyAdapter extends RecyclerView.Adapter<ThingyViewHolder> {

    private final ArrayList<Thingy> thingies;

    public ThingyAdapter(ArrayList<Thingy> thingies) {
        this.thingies = thingies;
    }

    @NonNull
    @Override
    public ThingyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ThingyViewHolder.create(parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ThingyViewHolder holder, int position) {
        holder.bind(thingies.get(position));
    }

    @Override
    public int getItemCount() {
        return thingies.size();
    }
}
