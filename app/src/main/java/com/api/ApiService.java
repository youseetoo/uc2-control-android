package com.api;

import com.api.response.MotorActRequest;
import com.api.response.MotorGetResponse;
import com.api.response.items.BtScanItem;
import com.api.response.LedArrRequest;
import com.api.response.LedArrResponse;
import com.api.response.MacRequest;
import com.api.response.WifiConnectRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/features_get")
    Call<String[]> getFeatures();

    @GET("/ledarr_get")
    Call<LedArrResponse> ledGet();

    @POST("/ledarr_act")
    Call<String> ledAct(@Body LedArrRequest request);


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
    @POST("/bt_paireddevices")
    Call<Void>removePairedDevice(@Body MacRequest mac);

    @GET("/motor_get")
    Call<MotorGetResponse>getMotorData();
    @POST("/motor_act")
    Call<Void>setMotorData(@Body MotorActRequest request);

    @GET("/control")
    Call<Void> setControl(@Query("var") String t, @Query("val") String val);

    /* 
     * ImSwitch fastapi
     */

     // positioner 
     // format PositionerController/movePositioner?positionerName=ESP32Stage&axis=Z&dist=100&isAbsolute=false&isBlocking=false&speed=10000' 
     
    @GET("/PositionerController/movePositioner")
    Call<Void> movePositioner(@Query("positionerName") String positionerName, @Query("axis") String axis, @Query("dist") int dist, @Query("isAbsolute") boolean isAbsolute, @Query("isBlocking") boolean isBlocking, @Query("speed") int speed);

    // laser
    // format http://localhost:8001/LaserController/setLaserValue?laserName=LED&value=1000'
    @GET("/LaserController/setLaserValue")
    Call<Void> setLaserValue(@Query("laserName") String laserName, @Query("value") int value);

}
