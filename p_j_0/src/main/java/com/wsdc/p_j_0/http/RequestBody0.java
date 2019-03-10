package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.http.io.IO;

import java.io.IOException;

/*
 *  请求体
 */
public abstract class RequestBody0 {
    protected IO bodyIO;
    Request0 request;

    /*
     *  将数据写入到源中
     *  <li>    这个函数是会多次调用的
     */
    public abstract int write(IO source) throws IOException;

    /*
     *  不同的请求体，contentType是不一样的
     *  <li>    不同的contentType，对数据的封装也是不一样的
     *  <li>    具体支持的请求头信息 存储在HttpGK之中
     */
    public abstract String contentType();

    //  返回具体的信息
    public abstract Object content();

    /*
     *  用于生成contentLength请求头信息
     *  <li>    -1  表示不使用ContentLength请求头
     *              使用chunked
     *
     *  <li>    0   表示请求体根本没有任何数据(空的)
     *
     *  <li>    >0  存在数据
     */
    public abstract int size();

    protected void setRequest(Request0 request) throws IOException {
        this.request = request;
        bodyIO = request.call.buffer();
    }

    public Request0 request(){
        return request;
    }

    public void finish(){
        bodyIO.finish();
    }

    public static final RequestBody0 EMPTY_BODY = new RequestBody0() {
        @Override
        public int write(IO source) throws IOException {
            return -1;
        }

        @Override
        public String contentType() {
            return null;
        }

        @Override
        public Object content() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }
    } ;
}
