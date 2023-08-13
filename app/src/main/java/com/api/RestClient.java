package com.api;

import android.util.Log;

import com.api.response.MotorActRequest;
import com.api.response.MotorGetResponse;
import com.api.response.items.BtScanItem;
import com.api.response.LedArrRequest;
import com.api.response.LedArrResponse;
import com.api.response.MacRequest;
import com.api.response.WifiConnectRequest;
import com.api.ws.Uc2WebSocket;

public class RestClient {

    ApiService apiService;
    String url;
    private String TAG = RestClient.class.getSimpleName();
    public RestClient(String url)
    {
        this.url = url;
        apiService = ApiServiceGenerator.createService(ApiService.class,"http://"+url+"/");
    }

    public Uc2WebSocket createWebSocket(String url)
    {
        Log.i(TAG,"createWebSocket ws://"+url);
        return new Uc2WebSocket(ApiServiceGenerator.getSharedClient(),"ws://"+url);
    }

    public String[] getFeatures() {
        Log.i(TAG,"getFeatures");
        return ApiServiceGenerator.executeSync(apiService.getFeatures());
    }

    public void getFeaturesAsync(ApiServiceCallback<String[]> callback)
    {
        Log.i(TAG,"getFeaturesAsync");
        apiService.getFeatures().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void getSsids(ApiServiceCallback<String[]> callback)
    {
        Log.i(TAG,"getSsids");
        apiService.getSsids().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void connectToWifi(WifiConnectRequest wifiConnectRequest, ApiServiceCallback<String> callback)
    {
        Log.d(TAG, wifiConnectRequest.toString());
        apiService.connectToWifi(wifiConnectRequest).enqueue(new ApiServiceCallbackAdapter<String>(callback));
    }

    public void resetNvFLash(ApiServiceCallback<Void> callback)
    {
        Log.i(TAG,"resetNvFLash");
        apiService.resetNvFlash().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void setLedArr(LedArrRequest request, ApiServiceCallback<String> callback)
    {
        Log.i(TAG,"setLedArr");
        apiService.ledAct(request).enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void getLedConfig(ApiServiceCallback<LedArrResponse> c)
    {
        Log.i(TAG,"getLedConfig");
        apiService.ledGet().enqueue(new ApiServiceCallbackAdapter<>(c));
    }

    public void scanForBtDevices(ApiServiceCallback<BtScanItem[]> callback)
    {
        Log.i(TAG,"scanForBtDevices");
        apiService.scanForBtDevices().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void getPairedBTDevices(ApiServiceCallback<BtScanItem[]> callback)
    {
        Log.i(TAG,"getPairedBTDevices");
        apiService.getPairedDevices().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void connectToBtDevice(MacRequest mac, ApiServiceCallback<Void> callback)
    {
        Log.i(TAG,"connectToBtDevice");
        apiService.connectToBtDevice(mac).enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void removePairedBtDevice(MacRequest mac, ApiServiceCallback<Void> callback)
    {
        Log.i(TAG,"removePairedBtDevice");
        apiService.removePairedDevice(mac).enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void getMotorData(ApiServiceCallback<MotorGetResponse> c)
    {
        apiService.getMotorData().enqueue(new ApiServiceCallbackAdapter<>(c));
    }
    public void setMotorData(MotorActRequest request,ApiServiceCallback<Void> c)
    {
        Log.i(TAG,"setMotorData");
        apiService.setMotorData(request).enqueue(new ApiServiceCallbackAdapter<>(c));
    }

    //EspCamera

    public void setControl(String type, String val,ApiServiceCallback<Void> c)
    {
        Log.i(TAG,"setMotorData type:"+ type + " Value:"+val);
        apiService.setControl(type,val).enqueue(new ApiServiceCallbackAdapter<>(c));
    }
}
