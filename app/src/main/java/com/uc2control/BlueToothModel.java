package com.uc2control;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.api.ApiServiceCallback;
import com.api.RestController;
import com.api.response.BtScanItem;
import com.api.response.MacRequest;

public class BlueToothModel extends BaseObservable
{
    private final RestController restController;

    private BtScanItem[] btScanItems =new BtScanItem[0];
    private String macAdress = "";

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

    public void connectToBTDevice()
    {
        if (!macAdress.isEmpty())
        {
            MacRequest mac = new MacRequest();
            mac.mac = macAdress;
            restController.getRestClient().connectToBtDevice(mac, new ApiServiceCallback<Void>() {
                @Override
                public void onResponse(Void response) {

                }
            });
        }
    }

    @Bindable
    public BtScanItem[] getBtScanItems() {
        return btScanItems;
    }

    public void setBtScanItems(BtScanItem[] btScanItems) {
        if (btScanItems == this.btScanItems)
            return;
        if(btScanItems == null)
            this.btScanItems =new BtScanItem[0];
        else
            this.btScanItems = btScanItems;
        notifyPropertyChanged(BR.btScanItems);
    }

    public void setMacAdress(String macAdress) {
        if (this.macAdress.equals(macAdress))
            return;
        this.macAdress = macAdress;
        notifyPropertyChanged(BR.macAdress);
    }

    @Bindable
    public String getMacAdress() {
        return macAdress;
    }
}
