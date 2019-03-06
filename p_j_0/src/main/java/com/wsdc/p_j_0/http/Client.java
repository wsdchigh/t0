package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.looper.Looper;
import com.wsdc.p_j_0.looper.LooperImpl;

/*
 *  客户端
 *  <li>    本质是一个异步操作
 */
public class Client {
    Worker worker;
    String protocol = HttpGK.PROTOCOL_HTTP_1_1;

    Looper looper = LooperImpl.getDefault();

    public void call(Request0 request0){

    }

    public SegmentPool getSegmentPool(){
        return null;
    }

    public IConnectionPool connectionPool(){
        return null;
    }


}
