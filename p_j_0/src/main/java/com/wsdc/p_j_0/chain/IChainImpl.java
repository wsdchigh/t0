package com.wsdc.p_j_0.chain;

import com.wsdc.p_j_0.thread0.AbstractWorkTread;
import com.wsdc.p_j_0.thread0.IThread;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 *  为了减少主线程的压力
 *  <li>    尽可能的将任务移动到IO线程中去执行
 */
public class IChainImpl implements IChain<Integer,Map<Integer,Object>> {
    /*
     *  任务池
     *  <li>    存放依赖任务，默认不会移除
     *  <li>    如果需要移除，需要手动调用函数去移除
     */
    private List<ITask<Integer,Map<Integer,Object>>> tasks = new LinkedList<>();

    /*
     *  存放消息队列
     *  <li>    任何任务处理完之后，所有的结果均存放在该结果里面
     *  <li>    在执行新的任务之前，会分发所有的信息
     */
    private Queue<Sem> sems = new LinkedList<>();
    private Lock lock = new ReentrantLock();

    //  线程不安全
    private Map<Integer,Object> objectMap = new TreeMap<>();
    /*
     *  通常是一个组件对应一个任务池
     *  <li>    通常是有多个组件存放在栈中，所以，为每一个组件去创建一个线程是不可取的
     *  <li>    设定一个公共的线程去执行任务
     *  <li>    如果有特殊的需要，可以去创建自己的独有线程
     *          <li>    通常公共插件是需要这样做的
     */
    public static IThread<ITask<Integer,Map<Integer,Object>>> innerThread = new AbstractWorkTread<ITask<Integer,Map<Integer,Object>>>() {

        @Override
        public void run0(ITask<Integer,Map<Integer,Object>> task) {
            /*
             *  轮到任务执行的时候
             *  <li>    此处为IO线程
             *  <li>    chain   是允许为null的
             */
            IChain<Integer, Map<Integer, Object>> chain = task.getIChain();
            task.setStatus(1);
            Exception e0 = null;
            try {
                task.execute0();
            } catch (Exception e) {
                e.printStackTrace();
                e0 = e;
                task.setStatus(-1);
            }

            if(e0 == null){
                chain.dispatch(task);
            }else{
                try {
                    task.exception(e0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //  调用任务执行完毕的
            //  一定要先标记自己  再去loop
            task.post();

            chain.loop();
        }
    };

    public IThread<ITask<Integer,Map<Integer,Object>>> t1;

    public IChainImpl() {
        this(new TreeMap<Integer,Object>());
    }

    public IChainImpl(Map<Integer,Object> map) {
        this(innerThread,map);
    }

    public IChainImpl(IThread<ITask<Integer,Map<Integer,Object>>> t1, Map<Integer,Object> map) {
        this.t1 = t1;
        this.objectMap = map;
    }

    List<ITask<Integer, Map<Integer, Object>>> mainList = new LinkedList<>();

    @Override
    public IChain<Integer, Map<Integer, Object>> doMain0(ITask<Integer, Map<Integer, Object>> task) {
        task.setIChain(this);
        mainList.add(task);
        return this;
    }

    @Override
    public IChain<Integer, Map<Integer, Object>> doMain0(Integer integer) {
        ITask<Integer, Map<Integer, Object>> task0 = new Task0.Builder()
                .taskKey(integer)
                .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                    @Override
                    public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {

                    }
                })
                .build();
        task0.setIChain(this);
        t1.doFirst(task0);
        return this;
    }

    @Override
    public IChain<Integer, Map<Integer, Object>> doThen(ITask<Integer, Map<Integer, Object>> task) {
        lock.lock();
        task.setIChain(this);
        tasks.add(task);
        lock.unlock();
        //System.out.println("then tasks size = "+tasks.size());
        return this;
    }

    @Override
    public IChain<Integer, Map<Integer, Object>> remove(ITask<Integer, Map<Integer, Object>> task) {
        lock.lock();
        task.setIChain(this);
        tasks.remove(task);
        lock.unlock();

        return this;
    }

    @Override
    public IChain<Integer, Map<Integer, Object>> remove(Integer integer) {
        List<ITask> tmpTasks = new LinkedList<>();
        lock.lock();
        for (ITask<Integer, Map<Integer, Object>> task : tasks) {
            //System.out.println("origin = "+task.getTaskKey()+"  now = "+integer);
            if(task.getTaskKey().intValue() == integer.intValue()){
                tmpTasks.add(task);
            }
        }
        //System.out.println("size = "+tasks.size());
        tasks.removeAll(tmpTasks);
        //System.out.println("size = "+tasks.size());
        lock.unlock();
        return this;
    }

    @Override
    public IChain<Integer, Map<Integer, Object>> remove(List<Integer> integers) {
        List<ITask> tmpTasks = new LinkedList<>();
        lock.unlock();
        for (ITask<Integer, Map<Integer, Object>> task : tasks) {
            for (Integer integer : integers) {
                if(task.getTaskKey().intValue() == integer.intValue()){
                    tmpTasks.add(task);
                    break;
                }
            }
        }
        tasks.removeAll(tmpTasks);
        lock.unlock();
        return this;
    }

    @Override
    public void start() {
        t1.doAll(mainList);
        mainList.clear();
    }

    @Override
    public void exit() {
        if(t1 != innerThread){
            t1.exit();
        }

        tasks.clear();
    }

    @Override
    public void dispatch(ITask<Integer,Map<Integer,Object>> task) {
        lock.lock();
        Sem sem0 = new Sem(task.getTaskKey(),0);
        sems.add(sem0);
        lock.unlock();
    }

    List<ITask<Integer,Map<Integer,Object>>> addList = new LinkedList<>();
    List<ITask<Integer,Map<Integer,Object>>> removeList = new LinkedList<>();

    @Override
    public void loop() {
        lock.lock();
        try{
            Sem sem0 = null;
            while((sem0 = sems.poll()) != null){
                for (ITask<Integer, Map<Integer,Object>> task : tasks) {
                    task.receive(sem0.key);
                    if(task.shouldExecute()){
                        addList.add(task);
                        if(!task.isMulti()){
                            removeList.add(task);
                        }
                    }
                }

            }
            t1.doAll(addList);
            tasks.removeAll(removeList);
            /*
            System.out.println("size0 = "+addList.size());
            for (ITask<Integer, Map<Integer, Object>> t0 : addList) {
                System.out.println(t0.getTaskKey());
            }
            System.out.println("size1 = "+removeList.size());
            */
            addList.clear();
            removeList.clear();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public ITask<Integer, Map<Integer, Object>> getTaskByKey(Integer integer) {
        ITask<Integer, Map<Integer, Object>> rtn = null;
        lock.lock();
        try{
            for (ITask<Integer, Map<Integer, Object>> task : tasks) {
                if(task.getTaskKey().intValue() == integer){
                    rtn = task;
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        lock.unlock();
        return rtn;
    }

    @Override
    public Map<Integer, Object> wrap() {
        return objectMap;
    }

    private static class Sem{
        public Sem() {
        }

        public Sem(Integer key, int rtn) {

            this.key = key;
            this.rtn = rtn;
        }

        Integer key;
        int rtn;
    }
}
