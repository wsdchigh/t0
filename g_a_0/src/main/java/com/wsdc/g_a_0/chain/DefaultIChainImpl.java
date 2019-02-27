package com.wsdc.g_a_0.chain;

import com.wsdc.g_a_0.thread0.WorkThread;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 *  为了减少主线程的压力
 *  <li>    尽可能的将任务移动到IO线程中去执行
 */
public class DefaultIChainImpl<K,D> implements IChain<K,D> {
    //  运行时主线程任务池
    private Queue<ITask<K,D>> runMainPool = new LinkedList<>();

    //  运行时IO线程任务池
    private Queue<ITask<K,D>> runIOPool = new LinkedList<>();

    //  存储未运行的任务池
    private Queue<ITask<K,D>> sparePool = new LinkedList<>();

    //  内置一个线程  需要主动去关闭
    public static WorkThread innerThread = new WorkThread();

    public WorkThread t1;

    private Lock lock = new ReentrantLock();

    private D d;

    @Override
    public void doMain(ITask<K, D> task) {
        t1.addWork(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                runMainPool.offer(task);
                lock.unlock();
            }
        });
    }

    @Override
    public void doIO(ITask<K, D> task) {
        t1.addWork(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                runIOPool.offer(task);
                lock.unlock();
            }
        });
    }

    @Override
    public void then(ITask<K, D> task) {
        t1.addWork(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                sparePool.offer(task);
                lock.unlock();
            }
        });
    }

    @Override
    public void cancel(ITask<K,D> task) {
        t1.addWork(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                if(sparePool.contains(task)){
                    sparePool.remove(task);
                }
                lock.unlock();
            }
        });
    }

    @Override
    public void start() {
        t1.addWork(new Runnable() {
            @Override
            public void run() {
                
            }
        });

        t1.addWork(new Runnable() {
            @Override
            public void run() {

                ITask task = null;
                //  已经移除出队列了
                while((task = runMainPool.poll()) != null){
                    int rtn = -1;
                    try {
                        rtn = task.execute(d);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    lock.lock();

                    List<ITask<K,D>> tmpMainList = new LinkedList<>();
                    List<ITask<K,D>> tmpIOList = new LinkedList<>();
                    for (ITask<K, D> t0 : sparePool) {
                        t0.receive(task,rtn);
                        if(t0.shouldExecute()){
                            if(t0.runInMain()){
                                tmpMainList.add(t0);
                            }else{
                                tmpIOList.add(t0);
                            }
                        }
                    }

                    sparePool.remove(tmpMainList);
                    sparePool.remove(tmpIOList);

                    runMainPool.addAll(tmpMainList);
                    runIOPool.addAll(tmpIOList);

                    lock.unlock();
                }
            }
        });
    }

    @Override
    public void exit() {

    }

    public void doInMain(ITask<K,D> task){

    }

    public static void exit0(){
        innerThread.close();
    }
}
