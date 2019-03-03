package com.wsdc.g_a_0.http;

public interface InterceptChain extends Intercept{
    void add(Intercept intercept);
}
