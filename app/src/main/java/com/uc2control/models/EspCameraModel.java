package com.uc2control.models;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.api.ApiServiceCallback;
import com.api.RestController;
import com.api.ws.Uc2WebSocketListner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uc2control.BR;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class EspCameraModel extends BaseObservable {
    private final String TAG = EspCameraModel.class.getSimpleName();
    private RestController restController;
    private int focusSlider = 0;
    private int framesize = 9;
    private int lamp =0;
    private boolean isConnected = false;
    private WebSocketController webSocketController;
    private String url;
    private SharedPreferences sharedPreferences;
    private final String key_url_control = "url_espcam";
    private final ObjectMapper mapper = new ObjectMapper();
    private boolean isRecording = false;
    private Bitmap bitmap;
    byte[] frameBytes;
    private Context context;

    public EspCameraModel(SharedPreferences preferences,Context context)
    {
        this.restController = new RestController();
        this.sharedPreferences = preferences;
        this.context = context;
        webSocketController = new WebSocketController(webSocketListner,restController,mapper);
        setEspCamUrl(sharedPreferences.getString(key_url_control,"192.168.4.1"));
    }

    private ApiServiceCallback<Void> emtpycallback = new ApiServiceCallback<Void>() {
        @Override
        public void onResponse(Void response) {

        }
    };

    @Bindable
    public int getFocus()
    {
        return focusSlider;
    }

    public void setFocus(int value)
    {
        if (value == focusSlider)
            return;
        focusSlider = value;
        restController.getRestClient().setControl("focusSlider",String.valueOf(value),emtpycallback);
        notifyPropertyChanged(BR.focus);
    }

    @Bindable
    public int getFramesize()
    {
        return framesize;
    }

    public void setFramesize(int value)
    {
        if (value == framesize && restController.getRestClient() != null)
            return;
        framesize = value;
        restController.getRestClient().setControl("framesize",String.valueOf(value),emtpycallback);
        notifyPropertyChanged(BR.framesize);
    }

    @Bindable
    public int getLamp()
    {
        return lamp;
    }

    public void setLamp(int value)
    {
        if (value == lamp)
            return;
        lamp = value;
        restController.getRestClient().setControl("lamp",String.valueOf(value),emtpycallback);
        notifyPropertyChanged(BR.lamp);
    }


    @Bindable
    public boolean getEspCameraConnected()
    {
        return isConnected;
    }

    void setEspCameraConnected(boolean connected)
    {
        this.isConnected = connected;
        notifyPropertyChanged(BR.espCameraConnected);
    }

    public void setEspCamUrl(String url) {
        if (url == this.url)
            return;
        this.url = url;
        restController.setUrl(url);
        sharedPreferences.edit().putString(key_url_control,url).apply();
        notifyPropertyChanged(BR.espCamUrl);
    }

    @Bindable
    public Bitmap getFrame()
    {
        return bitmap;
    }

    public void setFrame(Bitmap bitmap)
    {
        if(this.bitmap == bitmap)
            return;
        this.bitmap = bitmap;
        notifyPropertyChanged(BR.frame);
    }

    @Bindable
    public String getEspCamUrl() {
        return url;
    }


    public <T> void sendSocketMessage(T request)
    {
        webSocketController.sendSocketMessage(request);
    }

    public void pauseWebSocket()
    {
        webSocketController.pauseWebSocket();
        Log.d(TAG, "pausewebsocket");
    }

    public void resumeWebSocket()
    {
        webSocketController.resumeWebSocket();
        Log.d(TAG, "resumewebsocket");
    }

    public void onConnectButtonClick()
    {
        Log.i(TAG, "set url:" +url);
        restController.setUrl(url);
        if (restController.getRestClient() != null)
            webSocketController.create(url+":81");
    }

    public void startRecording() {
        // This assumes you have a method to get the latest frame from the stream
        isRecording = true;
        new Thread(() -> {
            while (isRecording) {
                // TODO: Need a callback on frames from the MJPEG stream here
                byte[] frame = getLatestFrameFromStream();
                if (frame != null) {
                    saveFrameToFile(frame);
                }
            }
        }).start();
    }

    public void stopRecording() {
        // We're relying on the thread in startRecording() to check this variable regularly
        isRecording = false;

    }

    public void snapImage(){
        new Thread(() -> {
            // TODO: Need a callback on frames from the MJPEG stream here
            byte[] frame = getLatestFrameFromStream();
            if (frame != null) {
                saveFrameToFile(frame);
            }
        }).start();
    }


    private final Uc2WebSocketListner webSocketListner = new Uc2WebSocketListner(){
        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosed(webSocket, code, reason);
            setEspCameraConnected(false);
            Log.d(TAG, "onClosed " + reason);
        }

        @Override
        public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosing(webSocket, code, reason);
            Log.d(TAG, "onCloseing " + reason);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            setEspCameraConnected(false);
            Log.d(TAG, "onFailure " + response + "," + t);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            super.onMessage(webSocket, text);

        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
            if (bytes != null) {
                frameBytes = bytes.toByteArray();
                setFrame(BitmapFactory.decodeByteArray(frameBytes,0,bytes.size()));
            }
        }

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            super.onOpen(webSocket, response);
            setEspCameraConnected(true);
            Log.d(TAG, "onOpen " + response.message());
        }
    };

    private void saveFrameToFile(byte[] frame) {
        // This assumes you have a folder to save the images in


        // FIXME: Need to change this to DCIM, but doesnt work
        String imageFolder = context.getFilesDir().getAbsolutePath()+"/DCIM/UC2";

        /*
        //File imageFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "matchboxscope");
        File imageFolder Environment.getExternalStoragePublicDirectory("ESPressoscope");
        File imageFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "matchboxscope");

        if (!imageFolder.exists() && !imageFolder.mkdirs()) {

        }
        */
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File imageFile = new File(imageFolder, "IMG_" + timestamp + ".jpg");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            fos.write(frame);
            Toast.makeText(context, "File stored: "+ "IMG_" + timestamp + ".jpg", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getLatestFrameFromStream() {
        return frameBytes;
    }



    private void saveIpAddress(String ip) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ipAddress", ip);
        editor.apply();
    }

    private String loadIpAddress() {
        return sharedPreferences.getString("ipAddress", "192.168.4.1"); // Returning an empty string if no value found
    }


}
