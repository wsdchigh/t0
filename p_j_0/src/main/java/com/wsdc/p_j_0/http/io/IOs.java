package com.wsdc.p_j_0.http.io;

import com.wsdc.p_j_0.http.Client;

public class IOs {
    public static IO buffer(Client client){
        return new BufferIO(client);
    }

    public static IO direct(Client client){
        return new DirectIO(client);
    }
}
