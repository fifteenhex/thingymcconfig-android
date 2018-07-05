package jp.thingy.thingymcconfig_android.adapter;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

import jp.thingy.thingymcconfig.model.Thingy;

public abstract class ObservableArrayListAdapter<IT, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected final ObservableArrayList<IT> list;

    protected ObservableArrayListAdapter(ObservableArrayList<IT> list) {
        this.list = list;
        //fixme this probably leaks the adapter
        list.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Thingy>>() {
            @Override
            public void onChanged(ObservableList<Thingy> sender) {
                ObservableArrayListAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<Thingy> sender, int positionStart, int itemCount) {
                ObservableArrayListAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeInserted(ObservableList<Thingy> sender, int positionStart, int itemCount) {
                ObservableArrayListAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeMoved(ObservableList<Thingy> sender, int fromPosition, int toPosition, int itemCount) {
                ObservableArrayListAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Thingy> sender, int positionStart, int itemCount) {
                ObservableArrayListAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public final int getItemCount() {
        return list.size();
    }
}
