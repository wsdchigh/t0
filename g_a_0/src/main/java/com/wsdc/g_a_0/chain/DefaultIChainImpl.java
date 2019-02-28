package com.wsdc.g_a_0.chain;

import com.wsdc.g_a_0.thread0.WorkThread;

import java.util.Deque;
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

    //  主任务队列
    //private Queue<ITask<K,D>> mainKey = new LinkedList<>();


    private Queue<ITask<K,D>> runningQueue = new LinkedList<>();

    /*
     *  运行队列(双向队列)
     *  <li>    如果任务已经激活，需要执行   (不一定是立即执行)
     *  <li>    主任务，从队头添加(优先级高一点)
     *  <li>    依赖任务的激活，从队尾添加   (优先级第一点)
     *
     *  <li>    poll    执行的时候一定会移除掉
     */
    private Deque<ITask<K,D>> runningDeque = new LinkedList<>();

    /*
     *  任务池
     *  <li>    存放依赖任务，默认不会移除
     *  <li>    如果需要移除，需要手动调用函数去移除
     */
    private List<ITask<K,D>> tasks = new LinkedList<>();

    /*
     *  通常是一个组件对应一个任务池
     *  <li>    通常是有多个组件存放在栈中，所以，为每一个组件去创建一个线程是不可以去的
     *  <li>    设定一个公共的线程去执行任务
     *  <li>    如果有特殊的需要，可以去创建自己的独有线程
     *          <li>    通常公共插件是需要这样做的
     */
    public static WorkThread innerThread = new WorkThread();

    public WorkThread t1;

    public DefaultIChainImpl(D d) {
        t1 = innerThread;
        this.d = d;
    }

    public DefaultIChainImpl(WorkThread t1,D d) {
        this.t1 = t1;
        this.d = d;
    }

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
    public void remove(ITask<K, D> task) {

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

    @Override
    public void dispatch(K k, int rtn) {

    }

    public void doInMain(ITask<K,D> task){

    }

    public static void exit0(){
        innerThread.close();
    }
}
