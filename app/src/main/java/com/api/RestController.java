package com.api;

import android.util.Log;

public class RestController
{
    private RestClient restClient;

    public RestController()
    {}

    public void setUrl(String url)
    {
        try{
            // FIXME: This may be a wrong URL
            restClient = new RestClient(url);
        }
        catch (Exception e){
            Log.e("SetURL", String.valueOf(e));
        }

    }

    public RestClient getRestClient()
    {
        return restClient;
    }
}
