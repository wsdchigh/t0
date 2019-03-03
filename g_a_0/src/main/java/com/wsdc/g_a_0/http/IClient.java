package com.wsdc.g_a_0.http;

import com.wsdc.g_a_0.http.bytes.SegmentPool;

public interface IClient {
    void sync(ICall call);
    void async(ICall call,CallBack cb);

    SegmentPool getSegmentPool();

    public interface CallBack{
        void success(IResponse response);

        void failure(Exception e);
    }
}
