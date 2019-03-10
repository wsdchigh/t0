package com.wsdc.p_j_0.http;

/*
 *  负责对Call的解析
 *  <li>    工作者，负责对Call进行处理
 */
public interface Worker{
    /*
     *  对请求进行编码
     *  <li>    将请求行和请求头全部写入到IByteData之中
     *  <li>    数据量不会很大，撑死4K
     */
    void request(ICall call);

    /*
     *  将请求体写入到IByteData之中
     *  <li>    如果数据量比较大，不要一次性写入
     *  <li>    这里会多次调用
     *          <li>    如果没有数据了，就不要做任何处理
     */
    void write(ICall call);

    /*
     *  对响应中的响应行和响应体进行读取
     *  <li>    如果读取完了，就不会再掉这个函数
     */
    void read(ICall call);

    /*
     *  响应体内容读取完毕，调用这个函数
     *  <li>    只调用一次
     *  <li>    不要在这里面执行IO操作
     *  <li>    这里还处于loop线程之中
     */
    void response(ICall call);
}
