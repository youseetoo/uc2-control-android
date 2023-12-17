package com.uc2control.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.api.ApiServiceCallback;
import com.api.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uc2control.BR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
public class ImSwitchCameraModel extends BaseObservable {
    private final String TAG = ImSwitchCameraModel.class.getSimpleName();
    private RestController restController;
    private int focusSlider = 100;
    private int framesize = 9;
    private int lamp =0;
    private boolean isConnected = false;
    private String url;
    private SharedPreferences sharedPreferences;
    private final String key_url_control = "url_imswitchcam";
    private final ObjectMapper mapper = new ObjectMapper();
    private boolean isRecording = false;
    private Bitmap bitmap;
    byte[] frameBytes;
    private Context context;
    private HandlerThread stream_thread;
    private Handler stream_handler;
    private final int ID_CONNECT = 200;
    
    public ImSwitchCameraModel(SharedPreferences preferences, Context context)
    {
        this.restController = new RestController();
        this.sharedPreferences = preferences;
        this.context = context;
        setImSwitchCameraUrl(sharedPreferences.getString(key_url_control,"192.168.2.191"));
    }

    private ApiServiceCallback<Void> emtpycallback = new ApiServiceCallback<Void>() {
        @Override
        public void onResponse(Void response) {

        }
    };

    public  void resetFocusTouch()
    {
        setFocus(100);
    }

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
        restController.getRestClient().setControl("focusSlider",String.valueOf(value-100),emtpycallback);
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
    public boolean getImSwitchCameraConnected()
    {
        return isConnected;
    }

    void setImSwitchCameraConnected(boolean connected)
    {
        this.isConnected = connected;
        notifyPropertyChanged(BR.imSwitchCameraConnected);
    }

    public void setImSwitchCameraUrl(String url) {
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
    public String getImSwitchCameraUrl() {
        return url;
    }

    private void createSocketListner()
    {
        if (stream_thread == null) {
            stream_thread = new HandlerThread("http");
            stream_thread.start();
            stream_handler = new HttpHandler(stream_thread.getLooper());
            stream_handler.post(() -> VideoStream());
        }
    }

    public void pauseWebSocket()
    {
        setImSwitchCameraConnected(false);
        if (stream_thread != null) {
            stream_handler = null;
            stream_thread.quitSafely();
            stream_thread = null;

        }
        Log.d(TAG, "pausewebsocket");
    }

    public void resumeWebSocket()
    {
        createSocketListner();
        Log.d(TAG, "resumewebsocket");
    }

    public void onConnectButtonClick()
    {
        Log.i(TAG, "set url:" +url);
        restController.setUrl(url);
        if (restController.getRestClient() != null) {
            resumeWebSocket();
        }
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


    private class HttpHandler extends Handler
    {
        public HttpHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
        }
    }


    private void VideoStream()
    {
        String stream_url = "http://" + url + ":8001/RecordingController/video_feeder";
        try
        {
            URL url = new URL(stream_url);
            try
            {
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("GET");
                huc.setConnectTimeout(1000 * 5);
                huc.setReadTimeout(1000 * 5);
                huc.setDoInput(true);
                huc.connect();

                if (huc.getResponseCode() == 200)
                {
                    setImSwitchCameraConnected(true);
                    InputStream in = huc.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(isr);
                    String data;

                    while ((data = br.readLine()) != null && isConnected)
                    {
                        //look up for the content-type
                        if (data.contains("--frame"))
                        {
                            //after that read length line, we dont need the length but it increase the buffer position about 1 line
                            data = br.readLine();
                            //after that the binary data starts and we can pass directly the inputstream because its at same position as the bufferedReader
                            setFrame(BitmapFactory.decodeStream(in));
                        }
                    }
                    try
                    {
                        if (br != null)
                        {
                            br.close();
                        }
                        if(in != null)
                        {
                            in.close();
                        }
                        stream_handler.sendEmptyMessageDelayed(ID_CONNECT,3000);
                    } catch (IOException e)
                    {
                        setImSwitchCameraConnected(false);
                        e.printStackTrace();
                    }
                }

            } catch (IOException e)
            {
                setImSwitchCameraConnected(false);
                e.printStackTrace();
            }
        } catch (MalformedURLException e)
        {
            setImSwitchCameraConnected(false);
            e.printStackTrace();
        } finally
        {
            setImSwitchCameraConnected(false);
        }

    }

}
