package com.wsdc.g_a_0.http.bytes;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 *  数据缓存池
 *  <li>    默认缓存64个Segment
 *  <li>    如果数据量大的话，最好多申请一点
 *
 *  <li>    这个实例不配置为静态，针对每一个IClient实力创建一个
 */
public class SegmentPool {
    private Queue<Segment> segments;
    int capacity = 0;
    int use = 0;

    private Lock lock = new ReentrantLock();

    public SegmentPool() {
        this(64);
    }

    public SegmentPool(int capacity) {
        this.capacity = capacity;
        segments = new LinkedList<>();
    }

    public Segment get(){
        Segment s = null;
        lock.lock();
        s = segments.poll();
        lock.unlock();

        if(s == null){
            s = new Segment();
        }else{
            use--;
        }
        s.status = 1;
        return s;

    }

    public void put(Segment segment){
        if(use >= capacity){
            return;
        }

        //  避免重复回收
        if(segment.status == 2){
            return;
        }

        segment.status = 2;

        lock.lock();
        if(use < capacity){
            use++;
            segment.recycle();
            segments.offer(segment);
        }
        lock.unlock();
    }
}
