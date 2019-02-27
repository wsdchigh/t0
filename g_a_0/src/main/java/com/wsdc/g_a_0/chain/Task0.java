package com.wsdc.g_a_0.chain;

import java.util.Arrays;
import java.util.List;

/*
 *  <li>    自身维系所有的状态
 *  <li>    任务依赖关系暂时只有三种    (key一律使用装箱类型Integer而不是int)
 *          <li>    relyKey     优先
 *          <li>    unionKey    其次
 *          <li>    orKeys      再者
 *          <li>    一个任务只需要一种依赖关系即可
 */
public abstract class Task0<K,D> implements ITask<K,D>{
    boolean hasExecute = false;
    public K taskKey;
    public K relyKey;

    //  注册依赖任务的返回值
    public int registerRelyRtn;
    public List<K> unionKeys;

    //  如果是联合多个任务，那么如果任务在执行完毕之后，在这里面标记  直到所有的标记均为true的时候才执行
    public List<Boolean> unionResults;

    //  这里不需要依赖任务结果返回池  因为只需要一个成功就可以了
    public List<K> orKeys;

    //  标记任务运行的线程
    public boolean runInMain = true;

    //  标记，下一波执行的时候，需要执行这个任务
    public boolean shouldExecute = false;

    @Override
    public int execute(D d) throws Exception{
        hasExecute = true;
        return execute0(d);
    }

    @Override
    public boolean shouldExecute() {
        return shouldExecute;
    }

    @Override
    public void receive(ITask task,int rtn) {
        /*
         *  视为命中
         */
        if(registerRelyRtn == rtn){
            if(relyKey != null){
                if(relyKey.equals(task.getTaskKey())){
                    shouldExecute = true;
                }
            }else if(unionKeys != null){
                int rtn0 = unionKeys.indexOf(task.getTaskKey());
                if(rtn0 != -1){
                    unionResults.add(true);
                    if(unionResults.size() == unionKeys.size()){
                        shouldExecute = true;
                    }
                }

            }else if(orKeys != null){
                if(orKeys.contains(task.getTaskKey())){
                    shouldExecute = true;
                }
            }
        }
    }

    @Override
    public K getTaskKey() {
        return taskKey;
    }

    @Override
    public boolean runInMain() {
        return runInMain;
    }

    protected abstract int execute0(D d) throws Exception;

    @Override
    public int hashCode() {
        if(taskKey instanceof Integer){
            return (int) taskKey;
        }
        return taskKey.hashCode();
    }

    private static class TaskInner<K,D> extends Task0<K,D>{
        TaskProxy t0;

        public TaskInner(TaskProxy t0) {
            this.t0 = t0;
        }

        @Override
        protected int execute0(D d)throws Exception {
            return t0.run(d);
        }
    }

    public static<K,D> Task0<K,D> doFirst(K taskKey, int registerRelyRtn, TaskProxy t0,  boolean runInMain){
        TaskInner ti = new TaskInner(t0);
        ti.taskKey = taskKey;
        ti.runInMain = runInMain;
        ti.registerRelyRtn = registerRelyRtn;
        return ti;
    }

    public static<K,D> Task0<K,D> doThen(K taskKey, K relyKey, int registerRelyRtn, TaskProxy t0, boolean runInMain){
        TaskInner ti = new TaskInner(t0);
        ti.taskKey = taskKey;
        ti.relyKey = relyKey;
        ti.runInMain = runInMain;
        ti.registerRelyRtn = registerRelyRtn;
        return ti;
    }

    public static<K,D> Task0<K,D> doUnion(K taskKey, int registerRelyRtn, TaskProxy t0,boolean runInMain, K... keys){
        TaskInner ti = new TaskInner(t0);
        ti.taskKey = taskKey;
        ti.unionKeys = Arrays.asList(keys);
        ti.runInMain = runInMain;
        ti.registerRelyRtn = registerRelyRtn;
        return ti;
    }

    public static<K,D> Task0<K,D> door(K taskKey, int registerRelyRtn, TaskProxy t0,boolean runInMain, K... keys){
        TaskInner ti = new TaskInner(t0);
        ti.taskKey = taskKey;
        ti.orKeys = Arrays.asList(keys);
        ti.runInMain = runInMain;
        ti.registerRelyRtn = registerRelyRtn;
        return ti;
    }


}
