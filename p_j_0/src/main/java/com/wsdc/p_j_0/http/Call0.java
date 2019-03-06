package com.wsdc.p_j_0.http;

public class Call0 implements ICall {
    Request0 request;
    Response0 response;
    Client client;
    IByteData source;
    IByteData sink;

    public Call0(Request0 request, Client client) {
        this.request = request;
        this.client = client;

        response = new Response0();
        source = new ByteDataImpl(client,new ByteDataImpl(client,null));
        sink = new ByteDataImpl(client,new ByteDataImpl(client,null));
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
    public IConnection connection() {
        return null;
    }

    @Override
    public IByteData sink() {
        return sink;
    }

    @Override
    public IByteData source() {
        return source;
    }

    @Override
    public void toQueue() throws Exception {

    }

    @Override
    public boolean loop() throws Exception {
        return false;
    }

    @Override
    public void exception(int status, Exception e) {

    }

    @Override
    public void requestExit() {

    }

    @Override
    public void exitQueue() throws Exception {

    }
}
