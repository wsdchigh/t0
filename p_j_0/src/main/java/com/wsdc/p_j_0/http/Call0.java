package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.http.io.IO;
import com.wsdc.p_j_0.http.io.IOs;

import java.io.IOException;

public class Call0 implements ICall {
    Request0 request;
    Response0 response;
    Client client;
    IO source;
    IO sink;
    IO buffer;
    IO buffer1;
    int tryCount = 4;
    Connection connection;
    int status = STATUS_REQUEST;
    Exception e;
    ICallback cb;


    public Call0(Request0 request, Client client) {
        this.request = request;
        this.client = client;

        response = new Response0(this);
        source = IOs.buffer(client);
        sink = IOs.buffer(client);
        buffer = IOs.buffer(client);
        buffer1 = IOs.buffer(client);

        request.prepare(this);
    }

    @Override
    public Request0 request() {
        return request;
    }

    @Override
    public Response0 response() {
        return response;
    }

    @Override
    public Client client() {
        return client;
    }

    @Override
    public Connection connection() {
        if(connection == null){
            connection = client.connectionPool().getConnection(this);
        }
        return connection;
    }

    @Override
    public IO sink() {
        return sink;
    }

    @Override
    public IO source() {
        return source;
    }

    @Override
    public IO buffer() {
        return buffer;
    }

    @Override
    public IO buffer1() {
        return buffer1;
    }


    @Override
    public int try0() {
        return tryCount--;
    }

    @Override
    public void execute() throws IOException {
        Connection connection = client.connectionPool().getConnection(this);
    }

    @Override
    public int status() {
        return status;
    }

    @Override
    public void setStatus(int newStatus) {
        status = newStatus;
    }

    @Override
    public void finish() {
        //  一次调用结束之后，执行该函数，回收数据
        if(e == null){
            //  成功

        }else{
            //  失败
        }

        source.finish();
        sink.finish();
        buffer.finish();
        buffer1.finish();
    }

    @Override
    public void exception(Exception e) {
        this.e = e;
    }

    @Override
    public ICallback callback() {
        return cb;
    }

    @Override
    public void setCallback(ICallback cb) {
        this.cb = cb;
    }
}
