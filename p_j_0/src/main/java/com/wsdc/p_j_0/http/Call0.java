package com.wsdc.p_j_0.http;

public class Call0 implements ICall {
    Request0 request;
    Response0 response;
    Client client;
    IByteData source;
    IByteData sink;
    int tryCount = 4;
    Segment segment;

    public Call0(Request0 request, Client client) {
        this.request = request;
        this.client = client;

        response = new Response0();
        source = new ByteDataImpl(client,new ByteDataImpl(client,null));
        sink = new ByteDataImpl(client,new ByteDataImpl(client,null));

        segment = client.getSegmentPool().get();
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
    public Segment headerSegment() {
        return segment;
    }

    @Override
    public int try0() {
        return tryCount--;
    }
}
