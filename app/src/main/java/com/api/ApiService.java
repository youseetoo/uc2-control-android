package com.api;

import com.api.response.MotorActRequest;
import com.api.response.MotorGetResponse;
import com.api.response.items.BtScanItem;
import com.api.response.LedArrRequest;
import com.api.response.LedArrResponse;
import com.api.response.LedSetRequest;
import com.api.response.MacRequest;
import com.api.response.MotorSetRequest;
import com.api.response.WifiConnectRequest;
import com.api.response.items.MotorGetItem;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/")
    Call<String[]> getFeatures();

    @GET("/ledarr_get")
    Call<LedArrResponse> ledGet();

    @POST("/ledarr_act")
    Call<String> ledAct(@Body LedArrRequest request);

    @POST("/ledarr_set")
    Call<String> ledSet(@Body LedSetRequest request);

    @GET("/wifi/scan")
    Call<String[]> getSsids();

    @Headers("Content-Type: application/json")
    @POST("/wifi/connect")
    Call<String> connectToWifi(@Body WifiConnectRequest wifiConnectRequest);

    @GET("/resetnv")
    Call<Void> resetNvFlash();

    @GET("/bt_scan")
    Call<BtScanItem[]> scanForBtDevices();

    @GET("/bt_paireddevices")
    Call<BtScanItem[]> getPairedDevices();
    
    @POST("/bt_connect")
    Call<Void>connectToBtDevice(@Body MacRequest mac);

    @POST("/motor_set")
    Call<Void>setMotorPins(@Body MotorSetRequest motorSetRequest);

    @GET("motor_get")
    Call<MotorGetResponse>getMotorData();

    @POST("motor_act")
    Call<Void>setMotorData(@Body MotorActRequest request);

}
