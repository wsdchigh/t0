package com.wsdc.p_j_0.http;

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
     *  用于读取非body的数据
     *  <li>    header requestLine responseLine数据不会超过1K(论单行)
     */
    Segment headerSegment();

    /*
     *  尝试次数
     *  <li>    通常一个Call会尝试连接4次
     */
    int try0();

    public interface ICallback{

    }
}
