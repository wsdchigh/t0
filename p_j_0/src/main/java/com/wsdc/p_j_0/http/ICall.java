package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.looper.LCall;

/*
 *  一个完整的调用信息
 *  <li>    request
 *  <li>    response
 *  <li>    connection
 */
public interface ICall extends LCall {
    Request0 request();
    Response0 response();
    Client client();
    IConnection connection();

    IByteData sink();
    IByteData source();
}
