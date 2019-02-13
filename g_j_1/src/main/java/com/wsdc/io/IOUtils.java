package com.wsdc.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
    public static byte[] read(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            byte[] cache = new byte[64];
            int len = 0;

            while((len = is.read(cache)) != -1){
                bos.write(cache,0,len);
            }

            final byte[] rtn = bos.toByteArray();
            return rtn;
        }finally {
            is.close();
            bos.close();
        }
    }

    public static void write(byte[] data, OutputStream os) throws IOException{
        InputStream is = new ByteArrayInputStream(data);
        write(is,os);
    }

    public static void write(InputStream is,OutputStream os) throws IOException{
        try{
            byte[] cache = new byte[64];
            int len = 0;

            while((len = is.read(cache)) != -1){
                os.write(cache,0,len);
                os.flush();
            }
        }finally {
            is.close();
            os.close();
        }
    }

}
