package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.http.io.IByteData;

import java.util.Map;

public interface IRequest {
    /*
     *  设置和移除头信息
     *  <li>    一些默认的请求头信息存储在HttpGK之中
     *  <li>    后续会根据配置选择存入
     */
    void header(String key, String value);

    Map<String,String> headers();

    String requestLine();

    String version();

    String method();

    String path();

    RequestBody0 body();

    ICall call();

    /*
     *  将数据写入到源中
     *  <li>    这样源才会有数据，才能对外提供数据
     */
    void write(IByteData source);
}
