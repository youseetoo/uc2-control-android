package com.uc2control.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.api.RestController;
import com.uc2control.models.EspCameraModel;
import com.uc2control.models.BlueToothModel;
import com.uc2control.models.ConnectionModel;
import com.uc2control.models.ImSwitchCameraModel;
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
    private EspCameraModel espCameraModel;
    private ImSwitchCameraModel imswitchCameraModel;


    @Inject
    public MainViewModel(RestController restController, SharedPreferences sharedPreferences,EspCameraModel espCameraModel, ImSwitchCameraModel imswitchCameraModel)
    {
        connectionModel = new ConnectionModel(restController,sharedPreferences);
        wifiSettingsModel = new WifiSettingsModel(restController);
        motorModel = new MotorModel(restController,connectionModel);
        ledModel = new LedModel(restController,connectionModel);
        blueToothModel = new BlueToothModel(restController);
        this.espCameraModel =  espCameraModel;
        this.imswitchCameraModel = imswitchCameraModel;
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
    public EspCameraModel getEspCameraModel(){return espCameraModel;}
    public ImSwitchCameraModel getImswitchCameraModel(){return imswitchCameraModel;}

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.d("MainViewModel", "onResume");
        DefaultLifecycleObserver.super.onResume(owner);
        try {
            // connectionModel.onConnectButtonClick(); // FIXME: Do not connect on startup ?
            connectionModel.resumeWebSocket();
            ledModel.getLedSettings();
            motorModel.getMotorData();
            espCameraModel.resumeWebSocket();
            imswitchCameraModel.resumeWebSocket();
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
        espCameraModel.pauseWebSocket();
        imswitchCameraModel.pauseWebSocket();
        Log.d("MainViewModel", "onPause");
    }
}
