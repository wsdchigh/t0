package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.looper.LCall;
import com.wsdc.p_j_0.looper.Looper;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocket;

/*
 *  链接
 *  <li>    连接，我们只需要InputStream/OutputStream
 *  <li>    里面如何实现(Socket)不需要暴露
 *
 *  <li>    这里是具体的连接
 *
 *  <li>    连接可以无法读写任何数据
 */
public class Connection implements LCall {
    private String host;
    private String port;
    private Socket socket;
    private ICall call;
    private Looper looper;
    private Client client;

    //  记录缓存的空闲时间
    long spareTime = 0l;

    /*
     *  标记状态值
     *  <li>    0   初始状态    (未连接)
     *  <li>    1   准备连接    (开始连接，但是没有获取连接结果)
     *  <li>    2   连接成功    (结果显示连接成功)
     *  <li>    -1  连接失败    (结果显示连接失败)
     *  <li>    3   IO          (开始IO读取)
     *  <li>    -3  IO结束      (结束IO操作)
     */
    int status = 0;

    public static final int STATUS_INIT = 0;
    //public static final int STATUS_PREPARE = 1;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_CONNECTED_OK = 2;
    public static final int STATUS_CONNECTED_FAILURE = -1;
    public static final int STATUS_CONNECTED_IO = 3;
    public static final int STATUS_CONNECTED_IO_END = -3;

    public Connection(Client client) {
        this.client = client;
        looper = client.looper;
    }

    public InputStream inputStream() throws IOException{
        return socket.getInputStream();
    }

    public OutputStream outputStream() throws IOException{
        return socket.getOutputStream();
    }

    public void setCall(ICall call){
        this.call = call;
    }

    public ICall getCall(){
        return call;
    }

    /*
     *  ping网络是否可以连通
     *  <li>    SO_OOBINLINE    紧急数据包   (默认不开启)
     *          <li>    该标记发送的消息属于紧急消息，不会影响到征程IO流操作
     *          <li>    如果服务Socekt需要使用紧急数据，那么不会是正常IO流来处理这些数据
     *  <li>    我们可以选择ping一次
     *          <li>    判断网络是否连接正常
     *          <li>    在需要些数据的时候ping一次即可
     *          <li>    尽量别ping
     */
    public boolean ping(){
        boolean rtn = false;
        try {
            socket.sendUrgentData(0xff);
            rtn = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    /*
     *  连接需要使用连接线程
     *  <li>    使用连接线程去连接
     *  <li>    连接线程可以是IO线程 (尽量不要这样)
     */
    public boolean connect(){
        return false;
    }

    public int status(){
        return status;
    }

    /*
     *  连接是可以复用的
     *  <li>    该函数标识一次使用完结，处于空闲状态
     *  <li>    轮询中移除自身
     */
    public void complete(){

    }

    /*
     *  标记此次连接开始工作
     *  <li>    注册到轮询中
     */
    public void start(){

    }

    @Override
    public void toQueue() throws Exception {

    }

    /*
     *  两个线程均会调用这个函数
     */
    @Override
    public boolean loop() throws Exception {
        return false;
    }

    @Override
    public void exception(int status, Exception e) {

    }

    /*
     *  同一个地址+port最多缓存2个空闲连接
     *  <li>    如果需要退出，调用这个函数
     *  <li>    不要在这里关闭任何数据
     */
    @Override
    public void requestExit() {
        looper.unregister(this);
    }

    @Override
    public void exitQueue() throws Exception {
        inputStream().close();
        outputStream().close();
        socket.close();
    }
}
