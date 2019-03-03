package com.wsdc.g_a_0.http.intercept;

import com.wsdc.g_a_0.http.ICall;

import java.io.IOException;

/*
 *  创建链接
 *  <li>    如果存在已有的空闲链接，就使用这个链接
 *  <li>    如果没有就创建一个新的
 */
public class ConnectionIntercept extends AbstractIntercept {
    @Override
    public void intercept(ICall call) throws IOException {

    }
}
