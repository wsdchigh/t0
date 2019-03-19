package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.io.IO;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
 *
 *  <li>    默认设置
 *          <li>
 */
public class Request0{
    String url;
    String method = "GET";
    String requestLine;
    String host;
    int port = 0;
    String protocol;
    Map<String,String> headers = new HashMap<>();
    ICall call;
    RequestBody0 body;
    String path;

    int step = 0;

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
        return body;
    }

    public ICall call() {
        return call;
    }

    public void prepare(ICall call){
        this.call = call;
    }

    /*
     *  网络访问之前的调用
     *  <li>    true    表示已经处理，那么将不会走网络了
     */
    public boolean doBeforeNet(){
        //  暂时丢弃
        return false;
    }

    public int write(IO source) throws IOException{
        if("GET".equalsIgnoreCase(method)){
            //  get请求不需要请求体 一次性写入数据
            return writeGet(source);
        }else{
            return writePost(source);
        }

    }

    private int writeGet(IO source) throws IOException{
        if(step++ == 0){
            //IO buffer = call.buffer();
            StringBuilder sb = new StringBuilder();
            sb.append(requestLine)
                    .append("\r\n");

            Set<String> set = headers.keySet();
            for (String s : set) {
                String value = header(s);
                if(value != null){
                    sb.append(s)
                            .append(":")
                            .append(value)
                            .append("\r\n");
                }
            }

            sb.append("\r\n");
            byte[] bytes = sb.toString().getBytes();
            source.source(bytes);

            return 0;
        }
        return -1;
    }

    private int writePost(IO source) throws IOException{
        if(step++ == 0){
            //IO buffer = call.buffer();
            StringBuilder sb = new StringBuilder();
            sb.append(requestLine)
                    .append("\r\n");
            body.setCall(call);
            System.out.println("---------------");
            if(body.size() == -1){
                header("Transfer-Encoding","chunked");
            }else if(body.size() >= 0){
                header("Content-length",String.valueOf(body.size()));
            }else if(body.size() == -2){
                //  什么也不要添加
            }

            header("Content-type",body.contentType());

            Set<String> set = headers.keySet();
            for (String s : set) {
                String value = header(s);
                if(value != null){
                    sb.append(s)
                            .append(":")
                            .append(value)
                            .append("\r\n");
                }
            }

            sb.append("\r\n");
            byte[] bytes = sb.toString().getBytes();
            source.source(bytes);

            return 0;
        }else{
            return body.write(source);
        }
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

        public Request0 build(){
            if(r.url == null){
                throw new RuntimeException("url == null");
            }

            if(r.url.length() < 10){
                throw new RuntimeException("url invalid");
            }

            URI uri = URI.create(r.url);

            r.protocol = uri.getScheme();
            r.host = uri.getHost();
            if(uri.getQuery() != null){
                r.path = uri.getPath()+"?"+uri.getQuery();
            }else{
                r.path = uri.getPath();
                if(r.path == null || "".equals(r.path)){
                    r.path = "/";
                }
            }

           r.port = uri.getPort();
            if("https".equals(r.protocol)){
                if(r.port == -1)
                r.port = 443;
            }else if("http".equals(r.protocol)){
                if(r.port == -1)
                r.port = 80;
            }else{
                throw new RuntimeException("只支持http或者https  protocol = "+r.protocol);
            }

            r.requestLine = String.format(HttpGK.REQUEST_LINE_FORMAT,r.method,r.path,"HTTP/1.1");

            r.header("host",r.host);
            r.header("date",HttpGK.getTime());

            if("GET".equalsIgnoreCase(r.method)){

            }else{
                //r.header("Transfer-Encoding","chunked");
                if(r.body == null){
                    r.body = RequestBody0.EMPTY_BODY;
                }
            }

            return r;
        }
    }
}
