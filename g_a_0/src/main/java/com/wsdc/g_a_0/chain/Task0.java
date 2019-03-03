package com.wsdc.g_a_0.chain;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 *  <li>    自身维系所有的状态
 *  <li>    任务依赖关系暂时只有三种    (key一律使用装箱类型Integer而不是int)
 *          <li>    relyKey     优先
 *          <li>    unionKey    其次
 *          <li>    orKeys      再者
 *          <li>    一个任务只需要一种依赖关系即可
 */
public abstract class Task0<K,D> implements ITask<K,D>{
    public K taskKey;
    public K relyKey;

    //  注册依赖任务的返回值
    public int registerRelyRtn;
    public List<K> unionKeys;

    //  如果是联合多个任务，那么如果任务在执行完毕之后，在这里面标记  直到所有的标记均为true的时候才执行
    public List<Boolean> unionResults;

    //  这里不需要依赖任务结果返回池  因为只需要一个成功就可以了
    public List<K> orKeys;

    //  标记，下一波执行的时候，需要执行这个任务
    public boolean shouldExecute = false;

    //  标记是否多次执行
    public boolean isMulti = true;

    //
    IChain<K,D> chain;

    @Override
    public boolean shouldExecute() {
        return shouldExecute;
    }

    @Override
    public void receive(K k,int rtn) {
        /*
         *  视为命中
         */
        if(registerRelyRtn == rtn){
            if(relyKey != null){
                if(relyKey.equals(k)){
                    shouldExecute = true;
                }
            }else if(unionKeys != null){
                int rtn0 = unionKeys.indexOf(k);
                if(rtn0 != -1){
                    if(unionResults == null){
                        unionResults = new LinkedList<>();
                    }
                    unionResults.add(true);
                    if(unionResults.size() == unionKeys.size()){
                        shouldExecute = true;
                    }
                }
            }else if(orKeys != null){
                if(orKeys.contains(k)){
                    shouldExecute = true;
                }
            }
        }
    }

    @Override
    public boolean isMulti() {
        return isMulti;
    }

    @Override
    public K getTaskKey() {
        return taskKey;
    }

    @Override
    public int hashCode() {
        if(taskKey instanceof Integer){
            return (Integer) taskKey;
        }
        return taskKey.hashCode();
    }

    @Override
    public void setIChain(IChain<K, D> chain) {
        this.chain = chain;
    }

    @Override
    public IChain<K, D> getIChain() {
        return chain;
    }

    @Override
    public void modify(ITask<K, D> task) {
        shouldExecute = false;
    }

    TaskProxy<D> post;
    @Override
    public void post(ITask<K, D> task, D d) {
        if(post != null){
            try {
                post.run(task, d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class TaskInner extends Task0<Integer,Map<Integer,Object>>{
        //  具体的任务执行的功能函数包装者
        TaskProxy<Map<Integer,Object>> t0;

        @Override
        public int execute(Map<Integer,Object> d)throws Exception {
            return t0.run(this,d);
        }
    }

    public static class Builder{
        TaskInner inner = new TaskInner();

        public Builder taskKey(int taskKey){
            inner.taskKey = taskKey;
            return this;
        }

        public Builder thenKey(Integer relyKey){
            inner.relyKey = relyKey;
            return this;
        }

        public Builder unionKey(Integer... unionKeys){
            inner.unionKeys = Arrays.asList(unionKeys);
            return this;
        }

        public Builder orKey(Integer... orKeys){
            inner.orKeys = Arrays.asList(orKeys);
            return this;
        }

        public Builder registerRtnKey(int registerRtnKey){
            inner.registerRelyRtn = registerRtnKey;
            return this;
        }

        public Builder multi(boolean multi){
            inner.isMulti = multi;
            return this;
        }

        public Builder proxy(TaskProxy<Map<Integer,Object>> t0){
            inner.t0 = t0;
            return this;
        }

        public Builder post(TaskProxy<Map<Integer,Object>> post){
            inner.post = post;
            return this;
        }

        public ITask<Integer,Map<Integer,Object>> build(){
            return inner;
        }
    }


}
