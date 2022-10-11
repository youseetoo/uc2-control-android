package com.api;

import android.util.Log;

import com.api.response.MotorActRequest;
import com.api.response.MotorGetResponse;
import com.api.response.items.BtScanItem;
import com.api.response.LedArrRequest;
import com.api.response.LedArrResponse;
import com.api.response.LedSetRequest;
import com.api.response.MacRequest;
import com.api.response.MotorSetRequest;
import com.api.response.WifiConnectRequest;
import com.api.ws.Uc2WebSocket;

public class RestClient {

    ApiService apiService;
    String url;
    public RestClient(String url)
    {
        this.url = url;
        apiService = ApiServiceGenerator.createService(ApiService.class,"http://"+url+"/");
    }

    public Uc2WebSocket createWebSocket()
    {
        return new Uc2WebSocket(ApiServiceGenerator.getSharedClient(),"ws://"+url+":81");
    }

    public String[] getFeatures() {
        return ApiServiceGenerator.executeSync(apiService.getFeatures());
    }

    public void getFeaturesAsync(ApiServiceCallback<String[]> callback)
    {
        apiService.getFeatures().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void getSsids(ApiServiceCallback<String[]> callback)
    {
        apiService.getSsids().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void connectToWifi(WifiConnectRequest wifiConnectRequest, ApiServiceCallback<String> callback)
    {
        Log.d(RestClient.class.getSimpleName(), wifiConnectRequest.toString());
        apiService.connectToWifi(wifiConnectRequest).enqueue(new ApiServiceCallbackAdapter<String>(callback));
    }

    public void resetNvFLash(ApiServiceCallback<Void> callback)
    {
        apiService.resetNvFlash().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void setLedArr(LedArrRequest request, ApiServiceCallback<String> callback)
    {
        apiService.ledAct(request).enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void setLedConfig(LedSetRequest r, ApiServiceCallback<String> c)
    {
        apiService.ledSet(r).enqueue(new ApiServiceCallbackAdapter<>(c));
    }

    public void getLedConfig(ApiServiceCallback<LedArrResponse> c)
    {
        apiService.ledGet().enqueue(new ApiServiceCallbackAdapter<>(c));
    }

    public void scanForBtDevices(ApiServiceCallback<BtScanItem[]> callback)
    {
        apiService.scanForBtDevices().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void getPairedBTDevices(ApiServiceCallback<BtScanItem[]> callback)
    {
        apiService.getPairedDevices().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void connectToBtDevice(MacRequest mac, ApiServiceCallback<Void> callback)
    {
        apiService.connectToBtDevice(mac).enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void removePairedBtDevice(MacRequest mac, ApiServiceCallback<Void> callback)
    {
        apiService.removePairedDevice(mac).enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void setMotorPins(MotorSetRequest motorSetRequest, ApiServiceCallback<Void> c)
    {
        apiService.setMotorPins(motorSetRequest).enqueue(new ApiServiceCallbackAdapter<>(c));
    }

    public void getMotorData(ApiServiceCallback<MotorGetResponse> c)
    {
        apiService.getMotorData().enqueue(new ApiServiceCallbackAdapter<>(c));
    }

    public void setMotorData(MotorActRequest request,ApiServiceCallback<Void> c)
    {
        apiService.setMotorData(request).enqueue(new ApiServiceCallbackAdapter<>(c));
    }
}
