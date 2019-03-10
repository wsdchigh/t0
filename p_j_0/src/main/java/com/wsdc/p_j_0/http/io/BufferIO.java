package com.wsdc.p_j_0.http.io;

import com.wsdc.p_j_0.http.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 *  带有缓存的IO
 *  <li>    不会读到-1
 */
public class BufferIO extends IO {
    //byte[] cache = new byte[64];

    public BufferIO(Client client) {
        super(client);
    }

    @Override
    public int source(InputStream is) throws IOException {
        int size0 = Math.min(is.available(),64);
        int read = is.read(cache,0,size0);
        source(cache,0,read);
        return read;
    }

    @Override
    public int sink(OutputStream os) throws IOException {
        int size0 = Math.min(64,size);
        int sink = sink(cache, 0, size0);
        os.write(cache,0,sink);
        os.flush();
        return sink;
    }
}
