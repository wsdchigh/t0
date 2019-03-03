package com.wsdc.g_a_0.http;

/*
 *  一个完整的调用信息
 */
public interface ICall {
    IRequest request();
    IResponse response();
    IClient client();
    IConnection connection();
}
