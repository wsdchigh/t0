package com.wsdc.p_j_0.http;

import java.util.HashMap;
import java.util.Map;

/*
 *  描述一个完整的请求
 *  <li>    请求行
 *          <li>    请求协议及其版本
 *          <li>    请求路径
 *          <li>    请求方法
 *
 *  <li>    请求头
 *
 *  <li>    请求体
 *          <li>    请求体有两种格式
 *          <li>    请求头中携带contentType
 *          <li>    请求头中携带分块编码
 */
public class Request0{
    String url;
    String method;
    String requestLine;
    String host;
    int port = 80;
    String protocol;
    Map<String,String> headers = new HashMap<>();
    ICall call;
    RequestBody0 body;
    String path;

    public void header(String key, String value) {
        headers.put(key,value);
    }

    public String header(String key){
        return headers.get(key);
    }

    public Map<String, String> headers() {
        return headers;
    }

    public RequestBody0 body() {
        return null;
    }

    public ICall call() {
        return null;
    }

    /*
     *  这个函数可以使用多次
     *  <li>    如果请求体比较大，那么需要多次调用这个函数
     */
    public int write(IByteData source) {
        return -1;
    }

    public static class Builder{
        Request0 r = new Request0();
        public Builder url(String url){
            r.url = url;
            return this;
        }

        public Builder method(String method){
            r.method = method;
            return this;
        }

        public Builder header(String key,String value){
            r.header(key,value);
            return this;
        }

        public Builder body(RequestBody0 body){
            r.body = body;
            return this;
        }

        public Builder protocol(String protocol){
            r.protocol = protocol;
            return this;
        }

        public Request0 builder(){
            if(r.url == null){
                throw new RuntimeException("url == null");
            }

            if(r.url.length() < 10){
                throw new RuntimeException("url invalid");
            }

            String prefix = r.url.substring(0, 5);
            String s0 = null;
            if(HttpGK.HTTPS.equals(prefix)){
                r.port = 443;
                s0 = r.url.substring(7);
            }else if(prefix.startsWith(HttpGK.HTTP)){
                r.port = 80;
                s0 = r.url.substring(6);
            }else{
                throw new RuntimeException("protocol invalid = "+prefix);
            }

            int len0 = s0.indexOf('/');
            if(len0 == -1){
                len0 = s0.length();
            }
            String s1 = s0.substring(0,len0);
            String s2 = "";
            if(len0 == s0.length()){
                s2 = "/";
            }else{
                s2 = s0.substring(len0);
            }
            r.host = s1;
            r.path = s2;

            if(r.protocol == null){
                r.protocol = HttpGK.PROTOCOL_HTTP_1_1;
            }

            if(!(r.protocol.equals(HttpGK.PROTOCOL_HTTP_1_1) || r.protocol.equals(HttpGK.PROTOCOL_HTTP_2_0))){
                throw new RuntimeException("protocol not support");
            }

            r.requestLine = String.format(HttpGK.REQUST_LINE_FORMAT,r.method,r.path,r.protocol);

            return r;
        }
    }
}
