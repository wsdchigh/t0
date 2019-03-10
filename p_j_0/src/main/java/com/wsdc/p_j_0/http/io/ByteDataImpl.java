package com.wsdc.p_j_0.http.io;

import com.wsdc.p_j_0.http.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteDataImpl implements IByteData {
    Client client;
    Segment header;
    Segment footer;

    byte[] cache = new byte[64];

    int size = 0;

    InputStream is = new InputStream0();
    OutputStream os = new OutputStream0();

    public ByteDataImpl(Client client) {
        this.client = client;

    }

    @Override
    public InputStream inputStream() {
        return is;
    }

    @Override
    public OutputStream outputStream() {
        return os;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void source(byte[] data) throws IOException {
        os.write(data);
    }

    @Override
    public void source(byte[] data, int start, int end) throws IOException {
        os.write(data,start,end-start);
    }

    @Override
    public int sink(byte[] data) throws IOException {
        return is.read(data);
    }

    @Override
    public int source(InputStream is) throws IOException {
        //  会不会有问题
        if(is.available() > 0){
            int read = is.read(cache);
            if(read == -1){
                return read;
            }
            source(cache,0,read);
        }
        return 0;
    }

    @Override
    public int source(IByteData source) throws IOException {
        return 0;
    }

    @Override
    public int sink(OutputStream os) throws IOException {
        return 0;
    }

    @Override
    public int sink(IByteData sink) throws IOException {
        return 0;
    }

    @Override
    public byte[] bytes(int size) throws IOException{
        byte[] rtn = new byte[this.size];
        is.read(rtn);
        return rtn;
    }

    @Override
    public String string(int size) throws IOException{
        byte[] bytes = bytes(size);
        if(bytes.length == 0){
            return null;
        }
        return new String(bytes);
    }

    @Override
    public int readLine(Segment segment) throws IOException {
        int size = this.size >64?64: this.size;
        for (int i = 0; i < size; i++) {
            int read = inputStream().read();
            segment.write(read);
            if(read == '\n'){
                return 1;
            }
        }
        return -1;
    }

    @Override
    public int readBuffer(Segment segment) throws IOException {
        int size = this.size >64?64: this.size;
        for (int i = 0; i < size; i++) {
            int read = inputStream().read();
            segment.write(read);
        }
        return size==0?-1:size;
    }

    @Override
    public void exit() {
        Segment segment = header;
        while((segment != null)){
            client.getSegmentPool().put(segment);
            segment = header.next;
        }
    }


    private final class InputStream0 extends InputStream{

        @Override
        public int read() throws IOException {
            if(header == null){
                return -1;
            }
            int read = -1;
            if(header.available() == 0){
                Segment next = header.next;
                if(next == null){
                    client.getSegmentPool().put(header);
                    return read;
                }
                header = next;
                read = header.read();
            }else{
                read = header.read();
            }
            size--;
            if(size == 0){
                client.getSegmentPool().put(header);
                header = null;
                footer = null;
            }
            return read;
        }

        @Override
        public int available() throws IOException {
            return size;
        }
    }

    private final class OutputStream0 extends OutputStream{

        @Override
        public void write(int b) throws IOException {
            if(footer == null){
                //  存在footer!=null 但是header==null的情况
                header = footer = client.getSegmentPool().get();
            }
            if(footer.write(b) == 0){
                //System.out.println("创建新的segment");
                Segment segment = client.getSegmentPool().get();
                footer.next = segment;
                segment.prev = footer;
                footer = segment;
                segment.write(b);
            }

            size++;
        }
    }
}
