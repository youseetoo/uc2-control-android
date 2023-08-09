package com.uc2control.viewmodels;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.api.RestController;
import com.uc2control.models.BlueToothModel;
import com.uc2control.models.ConnectionModel;
import com.uc2control.models.LedModel;
import com.uc2control.models.MotorModel;
import com.uc2control.models.WifiSettingsModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel implements DefaultLifecycleObserver {
    private WifiSettingsModel wifiSettingsModel;
    private MotorModel motorModel;
    private LedModel ledModel;
    private BlueToothModel blueToothModel;
    private ConnectionModel connectionModel;


    @Inject
    public MainViewModel(RestController restController, SharedPreferences sharedPreferences)
    {
        connectionModel = new ConnectionModel(restController,sharedPreferences);
        wifiSettingsModel = new WifiSettingsModel(restController);
        motorModel = new MotorModel(restController,connectionModel);
        ledModel = new LedModel(restController,connectionModel);
        blueToothModel = new BlueToothModel(restController);

    }

    public WifiSettingsModel getWifiSettingsModel() {
        return wifiSettingsModel;
    }
    public MotorModel getMotorModel() {
        return motorModel;
    }
    public LedModel getLedModel() {
        return ledModel;
    }
    public BlueToothModel getBlueToothModel() {
        return blueToothModel;
    }

    public ConnectionModel getConnectionModel() {
        return connectionModel;
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.d("MainViewModel", "onResume");
        DefaultLifecycleObserver.super.onResume(owner);
        try {
            connectionModel.onConnectButtonClick();
            connectionModel.resumeWebSocket();
            ledModel.getLedSettings();
            //TODO request motor data from server
            motorModel.getMotorData();
        }
        catch (IllegalArgumentException ex)
        {
            connectionModel.setMessage(ex.getLocalizedMessage());
            ex.printStackTrace();
        }

    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onPause(owner);
        connectionModel.pauseWebSocket();
        Log.d("MainViewModel", "onPause");
    }
}
