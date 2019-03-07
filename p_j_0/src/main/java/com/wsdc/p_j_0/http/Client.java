package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.looper.Looper;
import com.wsdc.p_j_0.looper.LooperImpl;
import com.wsdc.p_j_0.thread0.AbstractWorkTread;
import com.wsdc.p_j_0.thread0.IThread;

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

    //  连接线程，专门负责连接
    IThread<Connection> connectionIThread = new AbstractWorkTread<Connection>() {
        @Override
        public void run0(Connection connection) {

        }
    } ;

    public void call(Request0 request0){

    }

    public SegmentPool getSegmentPool(){
        return null;
    }

    public ConnectionPool connectionPool(){
        return null;
    }


}
