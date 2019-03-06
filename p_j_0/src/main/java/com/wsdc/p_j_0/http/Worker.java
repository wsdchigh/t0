package com.wsdc.p_j_0.http;

/*
 *  负责对Call的解析
 *  <li>    工作者，负责对Call进行处理
 */
public interface Worker{
    /*
     *  对请求进行编码
     *  <li>    允许调用多次
     *  <li>    通常只会调用一次
     */
    void requestEncoded(ICall call);


    /*
     *  对响应进行解码
     *  <li>    允许调用多次
     *  <li>    一定户调用多次
     *  <li>    直到整个响应 流程走完
     */
    void responseDecoded(ICall call);
}
