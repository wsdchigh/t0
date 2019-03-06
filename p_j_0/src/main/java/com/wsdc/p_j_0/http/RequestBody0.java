package com.wsdc.p_j_0.http;

/*
 *  请求体
 */
public class RequestBody0 {
    /*
     *  将数据写入到源中
     *  <li>    这个函数是会多次调用的
     */
    void write(IByteData source){

    }

    /*
     *  不同的请求体，contentType是不一样的
     *  <li>    不同的contentType，对数据的封装也是不一样的
     *  <li>    具体支持的请求头信息 存储在HttpGK之中
     */
    String contentType(){
        return "";
    }
    /*
     *  请求体的长度
     *  <li>    如果设置了contentLength，是需要这个参数的
     */
    int size(){
        return 0;
    }
}
