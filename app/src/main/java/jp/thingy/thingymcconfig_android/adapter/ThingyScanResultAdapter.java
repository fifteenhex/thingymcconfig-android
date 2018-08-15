package jp.thingy.thingymcconfig_android.adapter;

import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import jp.thingy.thingymcconfig.model.ScanResponse;
import jp.thingy.thingymcconfig_android.viewholder.ThingyScanResultViewHolder;

public class ThingyScanResultAdapter extends ObservableArrayListAdapter<ScanResponse.ThingyScanResult, ThingyScanResultViewHolder> {

    public interface OnThingyScanResultSelectedListener {
        void onThingyScanResultSelected(ScanResponse.ThingyScanResult scanResult);
    }

    private final OnThingyScanResultSelectedListener listener;

    private ThingyScanResultAdapter(ObservableArrayList<ScanResponse.ThingyScanResult> list,
                                    OnThingyScanResultSelectedListener listener) {
        super(list);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ThingyScanResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ThingyScanResultViewHolder.create(parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ThingyScanResultViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    public static ThingyScanResultAdapter createAdapter(ObservableArrayList<ScanResponse.ThingyScanResult> scanResults,
                                                        OnThingyScanResultSelectedListener listener) {
        return new ThingyScanResultAdapter(scanResults, listener);
    }
}
