package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.http.io.SegmentPool;
import com.wsdc.p_j_0.looper.Looper;
import com.wsdc.p_j_0.looper.LooperImpl;
import com.wsdc.p_j_0.thread0.AbstractWorkTread;
import com.wsdc.p_j_0.thread0.IThread;

import java.io.IOException;

/*
 *  客户端
 *  <li>    本质是一个异步操作
 */
public class Client {
    Worker worker;

    String protocol = HttpGK.PROTOCOL_HTTP_1_1;

    //  地址缓存的连接的数量
    int addressCacheSize = 4;

    Looper looper = LooperImpl.getDefault();

    SegmentPool segmentPool;
    ConnectionPool connectionPool = new ConnectionPool(this);

    //  连接线程，专门负责连接
    IThread<Connection> connectionIThread = new AbstractWorkTread<Connection>() {
        @Override
        public void run0(Connection connection){
            ICall call = connection.getCall();
            if(call.try0() > 0){
                try{
                    connection.connect();
                    connection.setStatus(Connection.STATUS_CONNECTED_OK);
                    looper.register(connection);
                    System.out.println("链接成功");
                    return;
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("链接失败");
                connection.setStatus(Connection.STATUS_CONNECTED_FAILURE);
                connection.close();
            }
        }
    } ;

    public Client() {
        worker = new HttpWorker(this);
    }

    public void call(Request0 request0) throws IOException {
        ICall call = new Call0(request0,this);
        call.execute();
    }

    public void call(Request0 request0, ICall.ICallback cb) throws IOException {
        ICall call = new Call0(request0,this);
        call.setCallback(cb);
        call.execute();
    }

    public SegmentPool getSegmentPool(){
        if(segmentPool == null){
            segmentPool = new SegmentPool(100);
        }
        return segmentPool;
    }

    public ConnectionPool connectionPool(){
        return connectionPool;
    }

    public static class Builder{
        Client client = new Client();
    }


}
