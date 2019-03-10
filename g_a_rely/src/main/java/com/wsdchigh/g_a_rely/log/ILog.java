package com.wsdchigh.g_a_rely.log;

import java.io.File;

public interface ILog {
    void v(String tag,String record);
    void t(String tag,String record);
    void e(String tag,String record);
    void d(String tag,String record);

    //  是否打印系统日志    debug可以打印，release可以不打印
    void printSystem(boolean system);

    /*
     *  日志存放的地址
     *  <li>    是一个文件夹
     *  <li>    每天创建一个文件
     */
    void saveFile(File file);

    /*
     *  上传到服务器
     *  <li>    服务器可以拆分每一个日志
     */
    void upload(File[] files);
}
