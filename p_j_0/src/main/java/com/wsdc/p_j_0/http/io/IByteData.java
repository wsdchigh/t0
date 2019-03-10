package com.wsdc.p_j_0.http.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 *  数据存储中间件
 *  <li>    内部持有1到多个小型byte数组，避免数据量大，申请大的数组
 *          <li>    分片存储数据
 *  <li>    代替直接使用byte数组
 *          <li>    复用
 *          <li>    分片
 *          <li>    减少内存波动
 *
 *  source/sink
 *  <li>    source  源，参数是源  read
 *  <li>    sink    池，参数是池  write
 *  <li>    当前实例是一个数据容器(如同File)，可以读写
 *
 *  <li>    如果响应是大文件，不要走这个，需要直接走文件
 */
public interface IByteData {
    InputStream inputStream();
    OutputStream outputStream();
    int size();

    void source(byte[] data) throws IOException;
    void source(byte[] data, int start, int end) throws IOException;
    int sink(byte[] data) throws IOException;

    /*
     *  将inputStream中的数据读取出来
     *  <li>    如果数据量大，不要一次性读完
     *  <li>    如果is没有数据了，就返回-1
     *  <li>    该函数主要用来读取文件的    (上传文件)
     *
     *  <li>    这里由loop进行驱动
     */
    int source(InputStream is) throws IOException;

    int source(IByteData source) throws IOException;

    /*
     *  将当前的内容充当池，写到其他输出流中
     *  <li>    文件中
     *  <li>    网络IO流中
     */
    int sink(OutputStream os) throws IOException;

    int sink(IByteData sink) throws IOException;

    /*
     *  截取部分(全部)数据
     */
    byte[] bytes(int size) throws IOException;
    String string(int size) throws IOException;

    /*
     *  缓冲读取    (针对Response的函数)
     *  <li>    readLine    如果此时还在解析ResponseLine或者ResponseHeader，调用该函数获取一行
     *  <li>    readBuffer  如果此时处于解析ResponseBody，没有\n这个概念了
     *
     *  下面两个函数本质是一样的，均是读取response，一次读取最多64B
     */
    int readLine(Segment segment) throws IOException;
    int readBuffer(Segment  segment) throws IOException;

    void exit();
}
