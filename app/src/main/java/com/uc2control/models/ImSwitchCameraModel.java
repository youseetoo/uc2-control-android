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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
public class ImSwitchCameraModel extends BaseObservable {
    private final String TAG = ImSwitchCameraModel.class.getSimpleName();
    private RestController restController;
    private int focusSlider = 100;
    private int moveXSlider = 100;
    private int moveYSlider = 100;
    private int moveASlider = 100;

    private int framesize = 9;
    private int led = 0;
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

    public ImSwitchCameraModel(SharedPreferences preferences, Context context) {
        this.restController = new RestController();
        this.sharedPreferences = preferences;
        this.context = context;
        setImSwitchCameraUrl(null);
    }

    private ApiServiceCallback<Void> emtpycallback = new ApiServiceCallback<Void>() {
        @Override
        public void onResponse(Void response) {

        }
    };

    public void resetFocusTouch() {
        setFocus(100);
    }

    @Bindable
    public int getFocus() {
        return focusSlider;
    }

    @Bindable
    public int getMoveX() {
        return moveXSlider;
    }

    @Bindable
    public int getMoveY() {
        return moveYSlider;
    }

    @Bindable
    public int getMoveA() {
        return moveASlider;
    }


    public void setFocus(int value) {
        if (value == focusSlider)
            return;
        focusSlider = value;

        restController.getRestClient().movePositioner(value, 10000, false, emtpycallback);
        notifyPropertyChanged(BR.focus);
    }

    public void setMoveX(int value) {
        if (value == moveXSlider)
            return;
        moveXSlider = value;
        restController.getRestClient().movePositioner(value, 10000, false, emtpycallback);
        notifyPropertyChanged(BR.moveX);
    }

    public void setMoveY(int value) {
        if (value == moveYSlider)
            return;
        moveYSlider = value;
        restController.getRestClient().movePositioner(value, 10000, false, emtpycallback);
        notifyPropertyChanged(BR.moveY);
    }

    public void setMoveA(int value) {
        if (value == moveASlider)
            return;
        moveASlider = value;
        restController.getRestClient().movePositioner(value, 10000, false, emtpycallback);
        notifyPropertyChanged(BR.moveA);
    }

    @Bindable
    public int getFramesize() {
        return framesize;
    }

    public void setFramesize(int value) {
        if (value == framesize && restController.getRestClient() != null || url==null)
            return;
        framesize = value;
        restController.getRestClient().setControl("framesize", String.valueOf(value), emtpycallback);
        notifyPropertyChanged(BR.framesize);
    }

    @Bindable
    public int getLED() {
        return led;
    }

    public void setLED(int value) {
        if (value == led)
            return;
        led = value;
        restController.getRestClient().setLaserValue("LED", value, emtpycallback);
        notifyPropertyChanged(BR.led);
    }


    @Bindable
    public boolean getImSwitchCameraConnected() {
        return isConnected;
    }

    void setImSwitchCameraConnected(boolean connected) {
        this.isConnected = connected;
        notifyPropertyChanged(BR.imSwitchCameraConnected);
    }

    public void setImSwitchCameraUrl(String url) {
        if (url == this.url || url == null)
            return;
        this.url = url;
        restController.setUrl(url);
        sharedPreferences.edit().putString(key_url_control, url).apply();
        notifyPropertyChanged(BR.espCamUrl);
    }

    @Bindable
    public Bitmap getFrame() {
        return bitmap;
    }

    public void setFrame(Bitmap bitmap) {
        if (this.bitmap == bitmap)
            return;
        this.bitmap = bitmap;
        notifyPropertyChanged(BR.frame);
    }

    @Bindable
    public String getImSwitchCameraUrl() {
        return url;
    }

    private void createSocketListner() {
        if (stream_thread == null) {
            stream_thread = new HandlerThread("http");
            stream_thread.start();
            stream_handler = new HttpHandler(stream_thread.getLooper());
            stream_handler.post(() -> VideoStream());
        }
    }

    public void pauseWebSocket() {
        setImSwitchCameraConnected(false);
        if (stream_thread != null) {
            stream_handler = null;
            stream_thread.quitSafely();
            stream_thread = null;

        }
        Log.d(TAG, "pausewebsocket");
    }

    public void resumeWebSocket() {
        createSocketListner();
        Log.d(TAG, "resumewebsocket");
    }

    public void onConnectButtonClick() {
        Log.i(TAG, "set url:" + url);
        setImSwitchCameraUrl(sharedPreferences.getString(key_url_control, "192.168.2.191"));
        //restController.setUrl(url);
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

    public void snapImage() {
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
        String imageFolder = context.getFilesDir().getAbsolutePath() + "/DCIM/UC2";

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
            Toast.makeText(context, "File stored: " + "IMG_" + timestamp + ".jpg", Toast.LENGTH_SHORT).show();
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


    private class HttpHandler extends Handler {
        public HttpHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
        }
    }


    private void VideoStream() {
        if(url==null)
            return;
        String streamUrl = "http://" + url + ":8001/RecordingController/video_feeder";
        HttpURLConnection huc = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(streamUrl);
            huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.setConnectTimeout(5000);
            huc.setReadTimeout(5000);
            huc.setDoInput(true);
            huc.connect();

            if (huc.getResponseCode() == 200) {
                setImSwitchCameraConnected(true);
                inputStream = huc.getInputStream();

                String contentType = huc.getHeaderField("Content-Type");
                String boundary = extractBoundary(contentType);

                while (isConnected) {
                    byte[] frame = readMJPEGFrame(inputStream, boundary);
                    if (frame != null) {
                        setFrame(BitmapFactory.decodeByteArray(frame, 0, frame.length));
                    }
                }
            }
        } catch (IOException e) {
            setImSwitchCameraConnected(false);
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (huc != null) {
                    huc.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            setImSwitchCameraConnected(false);
        }
    }

    private String extractBoundary(String contentType) {
        String[] parts = contentType.split(";");
        for (String part : parts) {
            if (part.trim().startsWith("boundary=")) {
                return part.trim().substring("boundary=".length());
            }
        }
        return null;
    }
    private byte[] readMJPEGFrame(InputStream inputStream, String boundary) throws IOException {
        ByteArrayOutputStream frameBuffer = new ByteArrayOutputStream();
        int prevByte = -1;
        int curByte;
        String boundaryString = "--" + boundary;
        byte[] boundaryBytes = boundaryString.getBytes(StandardCharsets.UTF_8);
        int matchIndex = 0;

        while ((curByte = inputStream.read()) != -1) {
            frameBuffer.write(curByte);

            // Check for boundary string
            if (prevByte == boundaryBytes[matchIndex]) {
                matchIndex++;
                if (matchIndex == boundaryBytes.length) {
                    // Remove the boundary bytes from the frame buffer
                    byte[] frame = frameBuffer.toByteArray();
                    return Arrays.copyOf(frame, frame.length - boundaryBytes.length);
                }
            } else {
                matchIndex = 0;
            }
            prevByte = curByte;
        }
        return null;
    }

}