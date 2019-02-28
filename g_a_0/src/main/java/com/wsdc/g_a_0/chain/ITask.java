package com.wsdc.g_a_0.chain;

/*
 *  任务自身持有所有的状态值
 *  <li>    不是任务链去持有任务的相关信息
 *  <li>    关于Data  通常是一个单独的主任务(或者多个)共同持有一个data
 *          <li>    也可以是提供一个公共的data，用于数据的共享，此时只需要区分好key即可
 *
 *  <li>    任务默认执行多次
 *
 *  <li>    类型
 *          <li>    主任务 (没有任何以来)
 *                  <li>    只执行一次   (只能执行一次)
 *          <li>    依赖任务    (需要其他任务执行完毕，发送出来的信号，才能执行)
 *                  <li>
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

    void receive(K k,int rtn);

    K getTaskKey();

    boolean runInMain();

    /*
     *  是否支持多次使用
     *  <li>    一次性还是永久性
     */
    boolean isMulti();

    void setIChain(IChain<K,D> chain);

    IChain<K,D> getIChain();
}
