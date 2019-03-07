package com.wsdc.p_j_0.chain;

/*
 *  给Task提供代理功能
 */
public interface TaskProxy<K,D> {
    /*
     *  ITask   任务
     *  <li>    之所以携带这个参数，这个参数携带了IChain参数
     *          提供更多可能的操作
     *  <li>    保存一条完整的调用链
     *  <li>    提供NIO操作的空间
     *
     *  <li>    D可能不具备线程安全性，但是这个函数一定是IO线程中执行
     *          <li>    如果需要执行线程调度的功能，需要先将数据取出来，然后在...
     *          <li>    不要将D暴露在其他线程之中
     */
    void run(ITask<K, D> task) throws Exception;
}
