package com.wsdc.g_a_0.chain;

/*
 *  任务自身持有所有的状态值
 *  <li>    不是任务链去持有任务的相关信息
 *  <li>    关于Data  通常是一个单独的主任务(或者多个)共同持有一个data
 *          <li>    也可以是提供一个公共的data，用于数据的共享，此时只需要区分好key即可
 *
 *  <li>    任务只执行一次
 */
public interface ITask<K,D> {
    /*
     *  执行结果
     *  <li>    1   成功
     *  <li>    0   失败
     *  <li>    -1  异常
     */
    int execute(D d) throws Exception;

    /*
     *  是否可以执行
     */
    boolean shouldExecute();

    void receive(ITask task,int rtn);

    K getTaskKey();

    boolean runInMain();
}
