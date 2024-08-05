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
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
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
import com.api.ws.Uc2WebSocket;
import com.api.ws.Uc2WebSocketListner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uc2control.BR;
import com.uc2control.R;

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
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.inject.Inject;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class EspCameraModel extends BaseObservable {
    private final String TAG = EspCameraModel.class.getSimpleName();
    private RestController restController;
    private int focusSlider = 100;
    private int framesize = 9;
    private int lamp = 0;
    private boolean isConnected = false;
    private String url;
    private SharedPreferences sharedPreferences;
    private final String key_url_control = "url_espcam";
    private final ObjectMapper mapper = new ObjectMapper();
    private boolean isRecording = false;
    private Bitmap bitmap;
    byte[] frameBytes;
    private Context context;
    private HandlerThread stream_thread;
    private Handler stream_handler;
    private final int ID_CONNECT = 200;
    private Uc2WebSocket uc2WebSocket;
    private final BlockingQueue frameQueue = new ArrayBlockingQueue(2);
    private Thread drawingThread;

    public EspCameraModel(SharedPreferences preferences, Context context) {
        this.restController = new RestController();
        this.sharedPreferences = preferences;
        this.context = context;
        setEspCamUrl(sharedPreferences.getString(key_url_control, "http://192.168.4.1"));
        //setEspCamUrl(null); // Do not initialize it with the URL already - it'll try to connect right away
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

    public void setFocus(int value) {
        if (value == focusSlider)
            return;
        focusSlider = value;
        restController.getRestClient().setControl("focusSlider", String.valueOf(value - 100), emtpycallback);
        notifyPropertyChanged(BR.focus);
    }

    @Bindable
    public int getFramesize() {
        return framesize;
    }

    public void setFramesize(int value) {
        if (value == framesize && restController.getRestClient() == null)
            return;
        framesize = value;
        restController.getRestClient().setControl("framesize", String.valueOf(value), emtpycallback);
        notifyPropertyChanged(BR.framesize);
    }

    @Bindable
    public int getLamp() {
        return lamp;
    }

    public void setLamp(int value) {
        if (value == lamp)
            return;
        lamp = value;
        restController.getRestClient().setControl("lamp", String.valueOf(value), emtpycallback);
        notifyPropertyChanged(BR.lamp);
    }


    @Bindable
    public boolean getEspCameraConnected() {
        return isConnected;
    }

    void setEspCameraConnected(boolean connected) {
        this.isConnected = connected;
        notifyPropertyChanged(BR.espCameraConnected);
    }

    public void setEspCamUrl(String url) {
        if (url == this.url || url == null)
            return;
        this.url = url;
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
    public String getEspCamUrl() {
        return url;
    }

    private void createSocketListner() {
        if (restController == null || restController.getRestClient() == null)
            return;
        if (url.startsWith("ws")) {

            uc2WebSocket = restController.getRestClient().createWebSocket(url.replace("ws://", ""));
            uc2WebSocket.createNewWebSocket(uc2WebSocketListner);
        } else {
            if (stream_thread == null) {
                stream_thread = new HandlerThread("http");
                stream_thread.start();
                stream_handler = new HttpHandler(stream_thread.getLooper());
            }
            stream_handler.post(() -> VideoStream());
        }


    }

    private boolean timeToDraw = false;

    private void startDrawingThread() {
        timeToDraw = true;
        if (drawingThread == null) {
            drawingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (timeToDraw) {
                        try {
                            ByteBuffer byteBuffer = (ByteBuffer) frameQueue.take();
                            byte[] b = new byte[byteBuffer.remaining()];
                            byteBuffer.get(b);
                            Bitmap bitmap1 = BitmapFactory.decodeByteArray(b, 0, b.length);
                            setFrame(bitmap1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    drawingThread = null;
                }
            });
            drawingThread.start();
        }
    }

    private  void stopDrawingThread()
    {
        timeToDraw = false;
    }


    private Uc2WebSocketListner uc2WebSocketListner = new Uc2WebSocketListner() {
        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosed(webSocket, code, reason);
            Log.d(TAG, "onClosed " + reason);
            stopDrawingThread();
        }

        @Override
        public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosing(webSocket, code, reason);
            Log.d(TAG, "onClosing " + reason);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Exception e = (Exception) t;
            e.printStackTrace();
            stopDrawingThread();
        }

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            super.onOpen(webSocket, response);
            webSocket.send("s");
            Log.d(TAG, "onOpen");
            startDrawingThread();
            setEspCameraConnected(true);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            super.onMessage(webSocket, text);
            Log.d(TAG, "onMessage " + text);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
            ByteBuffer byteBuffer = bytes.asByteBuffer();
            if (frameQueue.size() == 2)
                frameQueue.poll();
            frameQueue.add(byteBuffer);
        }
    };

    public void pauseWebSocket() {
        setEspCameraConnected(false);
        try {
            uc2WebSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        restController.setUrl(url);
        sharedPreferences.edit().putString(key_url_control, url).apply();
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
        String stream_url = url;
        try {
            URL url = new URL(stream_url);
            try {
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("GET");
                huc.setConnectTimeout(1000 * 5);
                huc.setReadTimeout(1000 * 5);
                huc.setDoInput(true);
                huc.connect();

                if (huc.getResponseCode() == 200) {
                    setEspCameraConnected(true);
                    InputStream in = huc.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(isr);
                    String data;

                    while ((data = br.readLine()) != null && isConnected) {
                        //look up for the content-type
                        if (data.contains("Content-Type:")) {
                            //after that read length line, we dont need the length but it increase the buffer position about 1 line
                            data = br.readLine();
                            //after that the binary data starts and we can pass directly the inputstream because its at same position as the bufferedReader
                            setFrame(BitmapFactory.decodeStream(in));
                        }
                    }
                    try {
                        if (br != null) {
                            br.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                        stream_handler.sendEmptyMessageDelayed(ID_CONNECT, 3000);
                    } catch (IOException e) {
                        setEspCameraConnected(false);
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                setEspCameraConnected(false);
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            setEspCameraConnected(false);
            e.printStackTrace();
        } finally {
            setEspCameraConnected(false);
        }

    }

}
