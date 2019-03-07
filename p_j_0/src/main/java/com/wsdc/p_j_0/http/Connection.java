package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.looper.LCall;
import com.wsdc.p_j_0.looper.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
    private int port;
    private Socket socket;
    private ICall call;
    private Looper looper;
    private Client client;
    private InputStream inputStream;
    private OutputStream outputStream;

    private ConnectionPool.Address address;

    //  记录缓存的空闲时间
    long spareTime = 0l;

    byte[] cache = new byte[256];

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

    public Connection(Client client, ConnectionPool.Address address) {
        this.client = client;
        this.address = address;
        looper = client.looper;
    }

    public InputStream inputStream() throws IOException {
        return inputStream;
    }

    public OutputStream outputStream() throws IOException {
        return outputStream;
    }

    public void setCall(ICall call) {
        this.call = call;
        host = call.request().host;
        port = call.request().port;
    }

    public ICall getCall() {
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
    public boolean ping() {
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
     *
     *  <li>    根据是否为ssl决定使用哪个socket
     */
    public boolean connect() throws IOException {
        socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        return false;
    }

    public int status() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void toQueue() throws Exception {
        status = STATUS_CONNECTED_IO;
    }


    @Override
    public boolean loop() throws Exception {
        if(call != null){
            write0();
            read0();
            parse0();
            return true;
        }
       return false;
    }

    private void read0() throws IOException{
        if(inputStream.available() > 0){
            int read = inputStream.read(cache);
            call.sink().write(cache,0,read);
        }
    }

    private void write0() throws IOException{
        int write = call.request().write(call.source());
        int read = call.source().read(cache);
        if(read != -1){
            outputStream.write(cache,0,read);
            outputStream.flush();
        }
    }

    private void parse0() throws Exception{
        Segment segment = call.headerSegment();
        int read0 = call.sink().readLine(segment);
        if(read0 != -1){
            byte[] reset = segment.reset();
            //String s =
        }
    }

    @Override
    public void exception(int status0, Exception e) {
        status = STATUS_CONNECTED_IO_END;
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
        if (status == STATUS_CONNECTED_IO) {
            if (address.queue.size() < address.capacity) {
                address.queue.offer(this);
                spareTime = System.currentTimeMillis();
            }

            call = null;
        } else {
            close();
        }
    }

    public void close() {
        try {
            if (inputStream != null) {
                inputStream().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (outputStream != null) {
                outputStream().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
