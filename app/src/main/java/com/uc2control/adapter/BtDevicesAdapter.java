package com.uc2control.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.api.response.items.BtScanItem;
import com.uc2control.databinding.SimpleBtItemBinding;

public class BtDevicesAdapter extends ArrayAdapter<BtScanItem> {

    public BtDevicesAdapter(Context context, BtScanItem[] items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BtScanItem item = getItem(position);
        if (convertView == null) {
            SimpleBtItemBinding bt = SimpleBtItemBinding.inflate(LayoutInflater.from(getContext()));
            convertView = bt.getRoot();
            convertView.setTag(bt);
        }
        SimpleBtItemBinding bt = (SimpleBtItemBinding) convertView.getTag();
        if (bt != null)
            bt.setBtItem(item);
        return convertView;

    }
}
