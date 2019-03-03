package com.wsdc.g_a_0.http.impl;

import com.wsdc.g_a_0.http.ICall;
import com.wsdc.g_a_0.http.IRequest;
import com.wsdc.g_a_0.http.IRequestBody;

import java.util.HashMap;
import java.util.Map;

public class Request0 implements IRequest {
    private String url;
    private int port;
    private String protocal;
    private String path;
    private Map<String,String> headers = new HashMap<>();

    @Override
    public void header(String key, String value) {

    }

    @Override
    public Map<String, String> headers() {
        return null;
    }

    @Override
    public String requestLine() {
        return null;
    }

    @Override
    public String version() {
        return null;
    }

    @Override
    public String method() {
        return null;
    }

    @Override
    public String path() {
        return null;
    }

    @Override
    public IRequestBody body() {
        return null;
    }

    @Override
    public ICall call() {
        return null;
    }

    public static class Builder{
        public Builder url(String url){
            return this;
        }
    }
}
