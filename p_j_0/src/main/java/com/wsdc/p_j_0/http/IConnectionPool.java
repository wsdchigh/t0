package com.wsdc.p_j_0.http;

/*
 *  连接池
 */
public interface IConnectionPool {
    IConnection getIConnection(String address, int port);
}
