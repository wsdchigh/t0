package com.wsdc.p_j_0.http.io;

/*
 *  碎片  (存储数据)
 *  <li>    通常会有1或者多个碎片存储数据
 *  <li>    使用碎片而不是byte数组，是为了减少大规模的申请大内存数据
 *          <li>    缓存Segment，可以有效的减少内存波动
 *  <li>    内部使用
 *  <li>    实现缓冲效果,一次读写不会超过1KB
 */
public class Segment {
    public Segment next;
    public Segment prev;

    /*
     *  状态
     *  <li>    0   初始状态
     *  <li>    1   使用中
     *  <li>    2   已经回收
     */
    public int status = 0;

    //  读的起始位置
    int read;

    //  写的起始位置
    int write;

    //  最大容量    缓存设置为1KB
    //int capacity = 1024;
    int capacity = 1024;
    byte[] data0 = new byte[capacity];
    //byte[] cache = new byte[64];


    public void recycle(){
        next = null;
        prev = null;
        read = 0;
        write = 0;
    }

    /*
     *  返回的是读取的长度
     *  <li>    如果读取的长度比实际的长度小，那么表示当前Segment满载了，需要额外的Segment来装载
     */
    public int write(byte[] origin,int start,int end){
        //  ol  origin length  原先需要请求的长度
        int ol = end - start;

        //  nl  now length      现在能够提供的长度
        int nl = capacity - write;

        //  fl  final length    最终取值(最小)
        int fl = nl>ol?ol:nl;

        System.arraycopy(origin,start, data0,write,fl);
        write += fl;

        return fl;
    }

    /*
     *  注意事项
     *  <li>    int和byte之间的转换，保存的是数据而不是值
     *          <li>    byte的区间是 -128 127
     */
    public int write(int i){
        if(write < capacity){
            data0[write++] = (byte)(i & 0xff);
            return 1;
        }
        return 0;
    }

    public int read(){
        if(read < write){
            return data0[read++] & 0xff;
        }
        return -1;
    }

    //  实际读取的长度
    public int read(byte[] dest,int start,int end){
        int ol = end - start;
        int nl = write - read;
        int fl = nl>ol?ol:nl;
        System.arraycopy(data0,read,dest,start,fl);
        read += fl;
        return fl;
    }


    public int charAt(char c){
        int index = read;
        for(;index<write;index++){
            if(data0[index] == c){
                return index-read;
            }
        }
        return -1;
    }

    /*
     *  将当前一行读取到另外一个Segment
     *  <li>    最多读取64个字节
     *  <li>    如果没有遇到\n字符，表示成功读取到完整字符串，返回-1
     *          如果读到了\n，表示成功读取到字符串，停止读取，并且返回1

    public int readLine0(Segment segment){
        int size = source-sink;
        if(size == 0){
            return -1;
        }

        size = size>64?64:size;
        for (int i = 0; i < size; i++) {
            int read0 = sink();
            segment.source(read0);
            if(read0 == '\n'){
                return 1;
            }
        }
        return -1;
    }
    */

    public int available(){
        return write-read;
    }

    public byte[] reset(){
        int size = write-read;
        byte[] rtn = new byte[size];
        System.arraycopy(rtn,0,data0,read,size);
        read = write = 0;
        return rtn;
    }
}
