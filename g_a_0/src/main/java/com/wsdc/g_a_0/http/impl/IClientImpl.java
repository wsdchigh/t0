package com.wsdc.g_a_0.http.impl;

import com.wsdc.g_a_0.http.ICall;
import com.wsdc.g_a_0.http.IClient;
import com.wsdc.g_a_0.http.bytes.SegmentPool;

public class IClientImpl implements IClient {
    SegmentPool pool;

    public IClientImpl() {
        pool = new SegmentPool(128);
    }

    @Override
    public void sync(ICall call) {

    }

    @Override
    public void async(ICall call, CallBack cb) {

    }

    @Override
    public SegmentPool getSegmentPool() {
        return pool;
    }
}
