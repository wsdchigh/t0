package com.wsdc.p_j_0.thread0;

import java.util.Collection;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class AbstractWorkTread<A> implements IThread<A>{
    private Thread t;
    private boolean exit;

    /*
     *  关于队列
     *  <li>    非阻塞队列   (offer/poll)
     *  <li>    阻塞队列     (put/take)
     *
     *  <li>    队列默认是队尾存数据，队头去数据
     *
     *  双向队列
     *  <li>    遵循队列的默认行为，同时添加相反的操作
     *  <li>    putLast = put   putFirst
     *  <li>    takeFirst = take    takeLast
     *  <li>    扩展队列的灵活性
     */
    BlockingDeque<A> deque = new LinkedBlockingDeque<>();

    {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!exit){
                    try {
                        //  阻塞队列，得使用take    poll适用于非阻塞队列
                        A a = deque.take();
                        run0(a);

                        //  可能会炸CPU
                        Thread.currentThread().sleep(2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    @Override
    public void doFirst(A a) {
        try{
            deque.putFirst(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doLast(A a) {
        try {
            deque.putLast(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAll(Collection<A> c) {
        try{
            for (A a : c) {
                deque.putLast(a);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
        exit = true;
        /*
         *  因为线程已经阻塞了，此时需要打断线程
         *  <li>    while会捕获线程，执行下一次的轮询
         *          遇到exit参数，自动退出了
         */
        t.interrupt();
    }

    @Override
    public void reset() {
        deque.clear();
        t.interrupt();
    }
}
