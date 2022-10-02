package com.uc2control.viewmodels;

import androidx.lifecycle.ViewModel;

import com.api.RestController;
import com.uc2control.models.WifiSettingsModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WifiSettingsModelView extends ViewModel {

    private WifiSettingsModel wifiSettingsModel;

    @Inject
    public WifiSettingsModelView(RestController restController)
    {
        wifiSettingsModel = new WifiSettingsModel(restController);
    }

    public WifiSettingsModel getWifiSettingsModel() {
        return wifiSettingsModel;
    }
}
