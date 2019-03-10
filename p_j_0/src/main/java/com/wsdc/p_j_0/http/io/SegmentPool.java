package com.wsdc.p_j_0.http.io;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 *  数据缓存池
 *  <li>    默认缓存64个Segment
 *  <li>    如果数据量大的话，最好多申请一点
 *
 *  <li>    不同的client可以共享
 *          <li>    建议共享
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
        if(s == null){
            s = new Segment();
        }else{
            //System.out.println("缓存取出 size = "+segments.size());
            use--;
        }
        s.status = 1;
        lock.unlock();

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
            //System.out.println("成功缓存 size = "+segments.size());
        }
        lock.unlock();
    }
}
