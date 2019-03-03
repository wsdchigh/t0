package com.wsdc.g_a_0.http;

import java.io.IOException;

/*
 *  拦截器
 */
public interface Intercept {
    void intercept(ICall call) throws IOException;
    InterceptChain chain();
    void setChain(InterceptChain chain);
}
