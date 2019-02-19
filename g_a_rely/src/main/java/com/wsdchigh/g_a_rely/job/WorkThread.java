package com.wsdchigh.g_a_rely.job;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
    BlockingQueue<Runnable> queue = new LinkedBlockingQueue();

    {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!exit){
                    try {
                        Runnable r = queue.take();
                        r.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    public void addWork(Runnable r){
        queue.offer(r);
    }

    public void close(){
        exit = true;
    }

}
