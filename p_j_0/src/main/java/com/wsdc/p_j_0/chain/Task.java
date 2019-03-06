package com.wsdc.p_j_0.chain;

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
 *                  <li>    存放在list表中，一直存在
 *
 *  <li>    执行线程
 *          <li>    所有任务均在IO线程中执行
 *          <li>    post函数支持将数据传递到其他线程
 *
 *  <li>    泛型
 *          <li>    K   key 每一个任务均有自己独特的key，通常这个key是整数，字符串(不要去其他值)
 *          <li>    D   datum   数据存放表，通常是一个Map(可以是封装Map的对象，表达一个k=v的容器关系)
 */
public interface Task<K,D> {
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

    void receive(K k, int rtn);

    K getTaskKey();

    /*
     *  是否支持多次使用
     *  <li>    一次性还是永久性
     */
    boolean isMulti();

    void setIChain(Chain<K, D> chain);

    Chain<K,D> getIChain();

    /*
     *  任务执行完之后，调用这个函数
     *  <li>    任务执行完之后，需要修改状态为不可执行
     *          <li>    否则，每次loop，任务度标记为可执行，会导致无限执行
     *
     *  <li>    其他任务的执行完毕之后，可能会影响都这个任务的属性
     *          <li>    调用这个函数去修改其他task
     */
    void modify(Task<K, D> task);

    /*
     *   任务执行完之后，将数据抛出来
     *   <li>    可以post到其他线程
     *   <li>    如果想要在主线程中去处理数据
     *
     *   <li>    任务执行之后允许有一个额外的操作
     *   <li>    建议其他的操作在这个函数中执行
     */
    void post(Task<K, D> task, D d);
}
