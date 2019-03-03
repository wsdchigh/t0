package com.wsdc.g_a_0.http;

import java.util.Map;

public interface IRequest {
    /*
     *  设置和移除1
     */
    void header(String key,String value);

    Map<String,String> headers();

    String requestLine();

    String version();

    String method();

    String path();

    IRequestBody body();

    ICall call();
}
