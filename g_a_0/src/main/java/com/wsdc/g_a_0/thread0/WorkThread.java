package com.wsdc.g_a_0.thread0;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/*
 *  工作线程
 *  <li>    定制一个线程
 *          <li>    这个线程可以是一个特地的线程  (只为处理一些指定的服务,IO线程)
 *          <li>    这个线程可以处理一些杂物    (非主线程概念)
 *                  <li>    http下载任务
 *
 *  <li>    系统应该提供几个这样的线程，方便工作
 *
 *  <li>    可以通过handler的方式发送数据
 */
public class WorkThread {
    private Thread t;
    private boolean exit;
    BlockingDeque<Runnable> deque = new LinkedBlockingDeque<>();

    {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!exit){
                    try {
                        //  阻塞队列，得使用take    poll适用于非阻塞队列
                        Runnable r = deque.take();
                        r.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    /*
     *  阻塞队列    使用put执行     offer默认不会阻塞
     */
    public void addWork(Runnable r){
        try {
            deque.put(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doFirst(Runnable r){
        try{
            deque.putFirst(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doLast(Runnable r){
        try {
            deque.putLast(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        exit = true;

        /*
         *  因为线程已经阻塞了，此时需要打断线程
         *  <li>    while会捕获线程，执行下一次的轮询
         *          遇到exit参数，自动退出了
         */
        t.interrupt();
    }

}
