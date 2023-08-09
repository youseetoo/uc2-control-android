package com.uc2control.models;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.api.ApiServiceCallback;
import com.api.ApiServiceGenerator;
import com.api.RestController;
import com.api.ws.Uc2WebSocket;
import com.api.ws.Uc2WebSocketListner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uc2control.BR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class ConnectionModel extends BaseObservable {

    private final String TAG = ConnectionModel.class.getSimpleName();
    private final String key_url = "url";

    private boolean isConnected = false;
    private RestController restController;
    private SharedPreferences sharedPreferences;
    private String url;
    private String message;

    private Uc2WebSocket webSocket;
    ObjectMapper mapper = new ObjectMapper();
    public WebSocketMotorDataEvent motorDataEvent;

    interface WebSocketMotorDataEvent
    {
        void onMotorDataChanged(int id, int pos);
    }

    public ConnectionModel(RestController restController,SharedPreferences sharedPreferences)
    {
        this.restController = restController;
        this.sharedPreferences = sharedPreferences;
        setUrl(sharedPreferences.getString(key_url,"192.168.4.1"));
    }

    public void onConnectButtonClick()
    {
        restController.setUrl(url);
        restController.getRestClient().getFeaturesAsync(getFeatureCallback);
        if (restController.getRestClient() != null)
            webSocket = restController.getRestClient().createWebSocket();
        setMessage("Connecting....");
    }

    private ApiServiceCallback<String[]> getFeatureCallback = new ApiServiceCallback<String[]>() {
        @Override
        public void onResponse(String[] response) {
            Log.i(TAG, Arrays.toString(response));
            setMessage("Connected");
            setConnected(true);
        }

        @Override
        public void onFailure(Throwable cause) {
            ApiServiceCallback.super.onFailure(cause);
            setMessage("Failed To Connect");
            setConnected(false);
        }
    };

    @Bindable
    public boolean getConnected()
    {
        return isConnected;
    }

    void setConnected(boolean connected)
    {
        this.isConnected = connected;
        notifyPropertyChanged(BR.connected);
    }

    public void setUrl(String url) {
        if (url == this.url)
            return;
        this.url = url;
        sharedPreferences.edit().putString(key_url,url).apply();
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    public <T> void sendSocketMessage(T request)
    {
        try {
            webSocket.getWebSocket().send(mapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public void pauseWebSocket()
    {
        setConnected(false);
        if (webSocket == null)
            return;
        try {
            webSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "pausewebsocket");
    }

    public void resumeWebSocket()
    {
        if (webSocket == null)
            return;
        webSocket.createNewWebSocket(webSocketListner);
        Log.d(TAG, "resumewebsocket");
    }

    private final Uc2WebSocketListner webSocketListner = new Uc2WebSocketListner()
    {
        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosed(webSocket, code, reason);
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
            Log.d(TAG, "onFailure " + response + "," + t);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            super.onMessage(webSocket, text);
            try {
                JSONObject response = new JSONObject(text);

                if(response.has("steppers"))
                {
                    JSONArray stprs = response.getJSONArray("steppers");
                    for(int i = 0; i< stprs.length();i++)
                    {
                        JSONObject stp = stprs.getJSONObject(i);
                        int id = stp.getInt("stepperid");
                        int pos = stp.getInt("position");
                        if(motorDataEvent != null)
                            motorDataEvent.onMotorDataChanged(id,pos);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            super.onOpen(webSocket, response);
            Log.d(TAG, "onOpen " + response.message());
        }
    };
}
