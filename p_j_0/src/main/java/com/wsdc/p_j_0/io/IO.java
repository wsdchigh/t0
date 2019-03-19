package com.wsdc.p_j_0.io;

import com.wsdc.p_j_0.http.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 *  数据容器
 *  <li>    使用Segment作为底层存储
 *  <li>
 */
public abstract class IO {
    //Client client;
    SegmentPool pool;
    Segment header;
    Segment footer;
    InputStream is = new DefaultInputStream();
    OutputStream os = new DefaultOutputStream();

    byte[] cache = new byte[64];

    int size = 0;

    public IO(Client client) {
        //this.client = client;
        pool = client.getSegmentPool();
    }

    public IO(SegmentPool pool){
        this.pool = pool;
    }

    /*
     *  inputStream()
     *  outputStream()
     *  <li>    后续的操作均是通过该函数支撑
     *  <li>    可以修改具体的实现
     */

    public InputStream inputStream(){
        return is;
    }

    public OutputStream outputStream(){
        return os;
    }

    public int size(){
        return size;
    }

    public void source(byte[] data) throws IOException{
        outputStream().write(data);
    }

    public void source(byte[] data,int start,int end) throws IOException{
        outputStream().write(data,start,end-start);
    }

    /*
     *  从流中读取数据
     *  <li>    这个函数需要多次驱动
     *          <li>    -1  表示读完了
     */
    public abstract int source(InputStream is) throws IOException;

    /*
     *  本质同上
     */
    public int source(IO source) throws IOException{
        return source(source.inputStream());
    }

    public int sink(byte[] data) throws IOException{
        return inputStream().read(data);
    }

    public int sink(byte[] data,int start,int end) throws IOException{
        return inputStream().read(data,start,end-start);
    }

    public abstract int sink(OutputStream os) throws IOException;

    public int sink(IO sink) throws IOException{
        return sink(sink.outputStream());
    }

    /*
     *  在当前的数据池中读取最多size个字节到sink中
     *  <li>    一次性读完
     */
    public int sink(IO sink,int size) throws IOException{
        int size0 = 0;
        while(true){
            int len = Math.min(64,inputStream().available());
            len = Math.min(len,size-size0);

            int read = inputStream().read(cache,0,len);
            sink.source(cache,0,read);
            size0 += read;

            if(size0 == size){
                return size0;
            }

            if(inputStream().available() == 0){
                return size0;
            }
        }
    }

    public byte[] bytes() throws IOException{
        return bytes(size);
    }

    public byte[] bytes(int size) throws IOException{
        int size0 = Math.min(size,this.size);
        if(size0 < 0){
            return new byte[0];
        }
        byte[] rtn = new byte[size0];
        inputStream().read(rtn);
        return rtn;
    }

    public String string() throws IOException{
        if(size == 0){
            return null;
        }
        byte[] bytes = bytes();
        return new String(bytes);
    }

    /*
     *  读取一行
     *  <li>    一次最多读64B
     *  <li>    -1/0    没有读到一行
     *  <li>    >0      读到了一行
     */
    public int readLine(IO sink) throws IOException{
        int size0 = Math.min(size,64);
        //System.out.println("size0 = "+size0);
        for (int i = 0; i < size0; i++) {
            int read = inputStream().read();
            sink.outputStream().write(read);

            if(read == '\n'){
                return 1;
            }

            //System.out.println("结果 = "+((char) read)+"  value = "+read);
        }

        return -1;
    }

    public void readBuffer(IO sink) throws IOException{
        int size0 = Math.min(size,64);
        for (int i = 0; i < size0; i++) {
            int read = inputStream().read();
            sink.outputStream().write(read);
        }
    }

    public void finish(){
        if(header != null){
            Segment s = header;
            while(s != null){
                pool.put(s);
                s = s.next;
            }
        }
    }

    /*
     *  默认流操作
     *  读的过程中
     *  <li>    读取之前，判断Segment是否有数据可以读
     *          如果没有数据读，此时需要移动header，并且回收之前的header
     *
     *
     *  <li>    流之动态更新数据
     *          <li>    如果流中的数据是动态更新的，需要避免读到-1 需要先判断available
     *          <li>    如果流中的数据是静态的，可以直接读到-1
     */
    private final class DefaultInputStream extends InputStream{

        @Override
        public int read() throws IOException {
            int read = -1;
            if(header == null){
                return read;
            }

            if(size > 0){
                if(header.available() == 0){
                    Segment next = header.next;
                    pool.put(header);
                    if(next == null){
                        header = null;
                        footer = null;
                        return read;
                    }
                    header = next;
                    read = header.read();
                }else{
                    read = header.read();
                }

                size--;
            }

            return read;
        }

        @Override
        public int available() throws IOException {
            return size;
        }
    }

    private final class DefaultOutputStream extends OutputStream{

        @Override
        public void write(int b) throws IOException {
            /*
             *  写是不分 -1 的
             */
            if(footer == null){
                header = footer = pool.get();
            }
            if(footer.write(b) == 0){
                //System.out.println("创建新的segment");
                Segment segment = pool.get();
                footer.next = segment;
                segment.prev = footer;
                footer = segment;
                segment.write(b);
            }
            size++;
        }
    }
}
