package com.wsdc.g_a_0.chain;

/*
 *  给Task提供代理功能
 */
public interface TaskProxy<D> {
    int run(D d) throws Exception;
}
