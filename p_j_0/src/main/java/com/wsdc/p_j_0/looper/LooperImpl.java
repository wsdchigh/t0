package com.wsdc.p_j_0.looper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 *  默认轮询器实现
 *  <li>    注册队列    (Map表)
 *          <li>    所有的注册和取消注册均存放在这个表中，在新的一次loop的时候，会根据这里面的内容进行注册或者取消注册
 *          <li>    在Map中 value=true表示注册，false表示取消注册
 *          <li>    该队列对外开放，需要同步锁保证线程安全
 *
 *  <li>    IO队列     (List)
 *          <li>    loop轮询，会遍历里面的实例对应的方法
 *          <li>    loopCall中的loop函数可能会抛出异常，不能因为一个异常的抛出导致整个线程的崩溃
 *          <li>    该队列不对外开放，线程安全
 *
 *  <li>    控制器     status
 *          <li>    当前线程默认是死循环，每一次轮询均会验证这个数值，如果数值是退出，则会退出
 *          <li>    status由其他线程控制   (当前开启的线程是IO线程)
 *
 *  <li>    注意事项
 *          <li>    轮询器，用于执行非阻塞的操作，通常是IO流操作
 *          <li>    对于阻塞部分的操作，我们可以选择使用线程池进行处理
 */
public class LooperImpl implements Looper {
    //  提供一个默认的轮询器  方便操作
    private static Looper instance ;

    static Lock l = new ReentrantLock();

    public static Looper getDefault(){
        if(instance == null){
            l.lock();
            if(instance == null){
                instance = new LooperImpl(10);
                try{
                    instance.work();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            l.unlock();
        }
        return instance;
    }

    List<LCall> list = new LinkedList<>();
    Map<LCall,Boolean> tmpRegisterMap = new HashMap<>();
    Map<LCall,Exception> registerExceptionMap = new HashMap<>();
    Map<LCall,Exception> unregisterExceptionMap = new HashMap<>();
    Lock lock = new ReentrantLock();

    long waitTime = 100;

    public LooperImpl(long waitTime) {
        this.waitTime = waitTime;
    }

    /*
     *  状态
     *  <li>    0       没有启动
     *  <li>    1       工作中
     *  <li>    -1      退出
     */
    int status = 1;

    Thread t;

    @Override
    public void register(LCall lc) {
        if(status != -1){
            lock.lock();
            tmpRegisterMap.put(lc,true);
            lock.unlock();
        }
    }

    @Override
    public void unregister(LCall lc) {
        if(status != -1){
            lock.lock();
            tmpRegisterMap.put(lc,false);
            lock.unlock();
        }
    }

    @Override
    public void work() {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(status != -1){
                    //  如果临时表中有数据，移动到正式表中来
                    if(tmpRegisterMap.size() > 0){
                        lock.lock();
                        Set<LCall> set = tmpRegisterMap.keySet();
                        for (LCall key : set) {
                            Boolean value = tmpRegisterMap.get(key);

                            if(value.booleanValue()){
                                /*
                                 *  如果回调出现异常,不要直接抛出异常
                                 *  <li>    异常保存到异常表中，注册表轮询完毕之后再行反馈
                                 */
                                if(!list.contains(key)){
                                    list.add(key);

                                    try{
                                        key.toQueue();
                                    }catch (Exception e1){
                                        registerExceptionMap.put(key,e1);
                                    }
                                }
                            }else{
                                if(list.contains(key)){
                                    list.remove(key);

                                    try{
                                        key.exitQueue();
                                    } catch (Exception e) {
                                        unregisterExceptionMap.put(key,e);
                                    }
                                }
                            }

                        }
                        tmpRegisterMap.clear();
                        lock.unlock();
                    }

                    /*
                     *  之所以选择创建额外的表存储异常
                     *  <li>    注册表在迭代的过程中不要进行增删
                     *  <li>    异常表不对外公开，线程安全
                     */
                    if(registerExceptionMap.size()>0){
                        Set<LCall> keys = registerExceptionMap.keySet();
                        for (LCall call : keys) {
                            call.exception(LCall.EXCEPTION_START, registerExceptionMap.get(call));
                        }
                        registerExceptionMap.clear();
                    }

                    if(unregisterExceptionMap.size() > 0 ){
                        Set<LCall> keys = unregisterExceptionMap.keySet();
                        for (LCall call : keys) {
                            call.exception(LCall.EXCEPTION_END, unregisterExceptionMap.get(call));
                        }
                        unregisterExceptionMap.clear();
                    }

                    boolean rtn = false;

                    //  IO队列不会对外公布，一定能够保证线程安全，直接使用迭代器
                    for (LCall lc : list) {
                        boolean rtn0 = false;
                        Exception e0 = null;
                        try{
                            rtn0 = lc.loop();
                        }catch (Exception e){
                            e0 = e;
                        }
                        rtn = rtn|rtn0;
                        if(e0 != null){
                            lc.exception(LCall.EXCEPTION_LOOP,e0);
                        }
                    }

                    /*
                     *  如果loop的过程中，有任务产生了数据(true) 就可以不要休眠了
                     */
                    if(!rtn){
                        try {
                            Thread.currentThread().sleep(waitTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //  如果退出的话，需要清空所有的队列信息  避免内存泄漏
                lock.lock();
                tmpRegisterMap.clear();
                list.clear();
                lock.unlock();
            }
        });

        t.start();
    }

    @Override
    public void exit() {
        status = -1;
    }
}
