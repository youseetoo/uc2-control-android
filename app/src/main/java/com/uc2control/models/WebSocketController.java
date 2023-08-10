package com.uc2control.models;

import android.util.Log;

import com.api.RestController;
import com.api.ws.Uc2WebSocket;
import com.api.ws.Uc2WebSocketListner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class WebSocketController
{
    private String TAG = WebSocketController.class.getSimpleName();
    private Uc2WebSocket webSocket;
    private Uc2WebSocketListner webSocketListener;
    private RestController restController;
    private ObjectMapper mapper;

    public WebSocketController(Uc2WebSocketListner webSocketListner, RestController restController, ObjectMapper objectMapper)
    {
        this.webSocketListener = webSocketListner;
        this.mapper = objectMapper;
        this.restController = restController;
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

    public void create(String url)
    {
        if (restController.getRestClient() != null) {
            webSocket = restController.getRestClient().createWebSocket(url);
        }
    }

    public void pauseWebSocket()
    {
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
        webSocket.createNewWebSocket(webSocketListener);
        Log.d(TAG, "resumewebsocket");
    }
}