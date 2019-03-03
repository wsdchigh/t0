package com.wsdc.g_a_0.http;

/*
 *  连接池
 */
public interface IConnectionPool {
    IConnection getIConnection(String address,int port);
}
