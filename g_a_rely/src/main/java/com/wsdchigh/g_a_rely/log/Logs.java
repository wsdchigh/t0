package com.wsdchigh.g_a_rely.log;

import android.util.Log;

import java.io.File;

/*
 *  日志
 *  <li>    系统提供日志
 *  <li>    我们需要实时上传服务器的日志，检测APP的运行状态
 *  <li>    一个独立的线程实现日志打印
 *          <li>    存放在一个指定的文件中
 */
public class Logs implements ILog{
    boolean system;

    @Override
    public void v(String tag, String record) {
        if(system){
            Log.v(tag, record);
        }

    }

    @Override
    public void t(String tag, String record) {
        if(system){
            Log.i(tag, record);
        }
    }

    @Override
    public void e(String tag, String record) {
        if(system){
            Log.e(tag, record);
        }
    }

    @Override
    public void d(String tag, String record) {
        if(system){
            Log.d(tag, record);
        }
    }

    @Override
    public void printSystem(boolean system) {
        this.system = system;
    }

    @Override
    public void saveFile(File file) {

    }

    @Override
    public void upload(File[] files) {

    }
}
