package com.wsdc.g_a_0.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 *  数据中心
 *  <li>    请求的数据存放在该区域
 *  <li>    响应的数据存放在该区域
 */
public interface IByteData {
    InputStream inputStream();
    OutputStream outputStream();

    void write(byte[] data) throws IOException;
    int read(byte[] data) throws IOException;


    /*
     *  这面这3个函数需要谨慎操作
     *  <li>    网络传输不是一次性就传输完了
     *  <li>    流的长度就有不确定性
     *
     *  <li>    我们使用一个\r\n  区分一行
     *          连个\r\n区分body
     *          根据header中的指定字段，表示body的长度
     */
    byte[] bytes(int size) throws IOException;
    String string(int size) throws IOException;
    String readLine() throws IOException;
}
