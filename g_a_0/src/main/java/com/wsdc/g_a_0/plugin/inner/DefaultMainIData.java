package com.wsdc.g_a_0.plugin.inner;

import android.os.Looper;

import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IDataChangeListener;
import com.wsdc.g_a_0.plugin.IPlugin;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 *  data    组件数据载体
 *  <li>    全局的data
 *  <li>    parent的data
 *  <li>    自身的data
 *
 *  <li>    除了put get函数之外，其他函数，需要在同一个线程中调用(通常是主线程)
 *          <li>    避免不必要的并发影响
 */
public class DefaultMainIData implements IData<Integer> {
    IPlugin plugin;

    public DefaultMainIData(IPlugin plugin) {
        this.plugin = plugin;
    }

    private Map<Integer,Object> dataMap = new TreeMap<>();
    private List<IDataChangeListener<IData,Integer>> listeners = new LinkedList<>();

    //  锁住map
    private ReadWriteLock lock0 = new ReentrantReadWriteLock();

    //  锁住观察者
    private ReadWriteLock lock1 = new ReentrantReadWriteLock();

    @Override
    public void put(Integer key, Object value) {
        Lock lock = lock0.writeLock();
        lock.lock();
        dataMap.put(key,value);
        lock.unlock();
    }

    @Override
    public void put(Map<Integer, Object> values) {
        Lock lock = lock0.writeLock();
        lock.lock();
        dataMap.putAll(values);
        lock.unlock();
    }

    @Override
    public Object get(Integer key) {
        Lock lock = lock0.readLock();
        lock.lock();
        Object value = dataMap.get(key);
        lock.unlock();
        return value;
    }

    @Override
    public List<Object> get(List<Integer> keys) {
        List<Object> rtn = new LinkedList<>();
        Lock lock = lock0.readLock();
        lock.lock();
        for (Integer key : keys) {
            rtn.add(dataMap.get(key));
        }
        lock.unlock();
        return rtn;
    }

    @Override
    public IPlugin plugin() {
        return plugin;
    }

    @Override
    public void register(IDataChangeListener listener) {
        Lock lock = lock1.writeLock();
        lock.lock();
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
        lock.unlock();

        //  所有注册者注册期间，均会通知一次
        final IDataChangeListener<IData,Integer> l = listener;

        if(Looper.myLooper() != Looper.getMainLooper()){
            plugin.handler().post(new Runnable() {
                @Override
                public void run() {
                    l.notify0(IData.TYPE_INIT,-1,DefaultMainIData.this);
                }
            });
        }else{
            l.notify0(IData.TYPE_INIT,-1,this);
        }
    }

    @Override
    public void unregister(IDataChangeListener listener) {
        Lock lock = lock1.writeLock();
        lock.lock();
        listeners.remove(listener);
        lock.unlock();
    }

    @Override
    public void notify0(int type, Integer key) {
        final Integer key0 = key;
        if(Looper.myLooper() != Looper.getMainLooper()){
            plugin.handler().post(new Runnable() {
                @Override
                public void run() {
                    Lock lock = lock0.readLock();
                    lock.lock();
                    for (IDataChangeListener listener : listeners) {
                        listener.notify0(IData.TYPE_SINGLE,key0,DefaultMainIData.this);
                    }
                    lock.unlock();
                }
            });
        }else{
            Lock lock = lock0.readLock();
            lock.lock();
            for (IDataChangeListener listener : listeners) {
                listener.notify0(IData.TYPE_SINGLE,key0,this);
            }
            lock.unlock();
        }
    }
}
