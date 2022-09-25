package com.uc2control;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.api.ApiServiceCallback;
import com.api.RestController;
import com.api.response.BtScanItem;

public class BlueToothModel extends BaseObservable
{
    private final RestController restController;

    private BtScanItem[] btScanItems =new BtScanItem[0];

    public BlueToothModel(RestController restController)
    {
        this.restController = restController;
    }

    public void startScanForBTDevices()
    {
        restController.getRestClient().scanForBtDevices(scanBtCallback);
    }

    private ApiServiceCallback<BtScanItem[]> scanBtCallback = new ApiServiceCallback<BtScanItem[]>() {
        @Override
        public void onResponse(BtScanItem[] response) {
            setBtScanItems(response);
        }
    };

    @Bindable
    public BtScanItem[] getBtScanItems() {
        return btScanItems;
    }

    public void setBtScanItems(BtScanItem[] btScanItems) {
        if (btScanItems == this.btScanItems)
            return;
        this.btScanItems = btScanItems;
        notifyPropertyChanged(BR.btScanItems);
    }
}
