package com.example.mat.wavesv01;

/**
 * Created by Mat on 16-02-2017.
 */

public class Messages {

    private String mDevice;
    private String mMessage;
    private Long mTime;
    private String mUrl;

    public Messages (String device, String message, Long time, String url){
        mDevice = device;
        mMessage = message;
        mTime = time;
        mUrl = url;
    }

    public String getDevice(){
        return mDevice;
    }

    public String getMessage(){
        return mMessage;
    }

    public Long getTime() {
        return mTime*1000;
    }

    public String getUrl() { return mUrl; }
}



