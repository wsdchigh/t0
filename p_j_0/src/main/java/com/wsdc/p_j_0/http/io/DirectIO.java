package com.wsdc.p_j_0.http.io;

import com.wsdc.p_j_0.http.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 *  直接IO
 *  <li>    会读到-1 不判断长度
 */
public class DirectIO extends IO {
    //byte[] cache = new byte[64];

    public DirectIO(Client client) {
        super(client);
    }

    public DirectIO(SegmentPool pool) {
        super(pool);
    }

    @Override
    public int source(InputStream is) throws IOException {
        int read = is.read(cache);
        if(read == -1){
            return read;
        }
        source(cache,0,read);
        return read;
    }

    @Override
    public int sink(OutputStream os) throws IOException {
        int read = is.read(cache);
        if(read == -1){
            return read;
        }
        os.write(cache,0,read);
        os.flush();
        return read;
    }
}
