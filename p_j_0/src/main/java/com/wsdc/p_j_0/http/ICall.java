package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.http.io.IO;

import java.io.IOException;

/*
 *  一个完整的调用信息
 *  <li>    request
 *  <li>    response
 *  <li>    connection
 *
 *
 *  执行周期
 *  <li>    发送request
 *  <li>    接收response
 */
public interface ICall{
    public static final int STATUS_REQUEST = 1;
    public static final int STATUS_RESPONSE_HEADER = 2;
    public static final int STATUS_RESPONSE_BODY = 3;
    public static final int STATUS_END = 4;

    Request0 request();
    Response0 response();
    Client client();
    Connection connection();

    //  response的数据写入到这里
    IO sink();

    //  request的数据写入到这里
    IO source();

    /*
     *  缓冲IO
     *  <li>    requestBody中的数据线写入到这里，然后在写到source中
     *  <li>    responseBody中的数据先写入到这里，然后在写到sink中
     */
    IO buffer();

    /*
     *  缓冲IO
     */
    IO buffer1();

    /*
     *  尝试次数
     *  <li>    通常一个Call会尝试连接4次
     */
    int try0();

    void execute() throws IOException;

    /*
     *  状态
     *  <li>    请求阶段
     *  <li>    响应读取responseLine和responseHeader阶段
     *  <li>    响应读取responseBody阶段
     *  <li>    完成
     */
    int status();

    void setStatus(int newStatus);

    /*
     *  结束
     *  <li>    调用结束
     */
    void finish();

    void exception(Exception e);

    ICallback callback();

    void setCallback(ICallback cb);

    public interface ICallback{
        /*
         *  成功回调
         *  <li>    当前线程处于IO线程,不适宜进行大量的IO延时操作
         *          <li>    true，表示我需要时间处理，之后会自己去关闭资源
         *          <li>    false 我已经处理完了，可以关闭资源了
         */
        boolean success(ICall call) throws IOException;

        void failure(ICall call,Exception e) throws IOException;
    }
}
