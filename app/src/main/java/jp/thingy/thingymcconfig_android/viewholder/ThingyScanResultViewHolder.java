package jp.thingy.thingymcconfig_android.viewholder;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import jp.thingy.thingymcconfig.model.ScanResponse;
import jp.thingy.thingymcconfig_android.R;
import jp.thingy.thingymcconfig_android.adapter.ThingyScanResultAdapter;
import jp.thingy.thingymcconfig_android.databinding.ViewholderThingyscanresultBinding;

public class ThingyScanResultViewHolder extends DataBindingViewHolder<ViewholderThingyscanresultBinding> {

    protected ThingyScanResultViewHolder(ViewholderThingyscanresultBinding binding) {
        super(binding);
    }

    public static ThingyScanResultViewHolder create(Context context) {
        ViewholderThingyscanresultBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.viewholder_thingyscanresult, null, false);
        return new ThingyScanResultViewHolder(binding);
    }

    public void bind(ScanResponse.ThingyScanResult scanResult,
                     ThingyScanResultAdapter.OnThingyScanResultSelectedListener listener) {
        binding.setScanResult(scanResult);
        binding.setListener(listener);
    }
}
