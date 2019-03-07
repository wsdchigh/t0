package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.looper.LCall;

/*
 *  一个完整的调用信息
 *  <li>    request
 *  <li>    response
 *  <li>    connection
 */
public interface ICall{
    Request0 request();
    Response0 response();
    Client client();
    Connection connection();

    IByteData sink();
    IByteData source();

    /*
     *  尝试次数
     *  <li>    通常一个Call会尝试连接4次
     */
    int try0();
}
