package com.wsdc.p_j_0.http.io;

import com.wsdc.p_j_0.http.Client;

public class IOs {
    public static IO buffer(Client client){
        return new BufferIO(client.getSegmentPool());
    }

    public static IO direct(Client client){
        return new DirectIO(client.getSegmentPool());
    }

    public static IO buffer(SegmentPool pool){
        return new BufferIO(pool);
    }

    public static IO direct(SegmentPool pool){
        return new DirectIO(pool);
    }
}
