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
 *
 *  <li>    执行线程
 *          <li>    所有任务均在IO线程中执行
 *          <li>    post函数支持将数据传递到其他线程
 */
public interface ITask<K,D> {
    /*
     *   执行的具体函数
     *   <li>    我们侧重在于or/union的执行力，并不是成功的多种可能性
     *          <li>    如果需要给成功定义一个结果，可以存放在D中进行处理
     *
     *  <li>    捕获或者自身判断，均自由抛出异常
     *          <li>    chain在执行任务的时候，自己会捕获异常，然后调用exception函数抛出
     */
    void execute0() throws Exception;

    /*
     *  失败
     *  <li>    自身任务在执行的时候，出现的错误(捕获到的异常)
     *  <li>    此时视为失败，不会下发任何信号
     *          <li>    在这里解决自身失败所需要进行的处理   (清理数据，重新开始之类)
     *  <li>    如果自身出现了错误，任务将不再往下分发信号
     */
    void exception(Exception e) throws Exception;

    /*
     *  是否可以执行
     */
    boolean shouldExecute();

    /*
     *  如果任务执行完之后，均会调用队列中的待处理任务的该函数
     *  <li>    判断自身是否需要执行
     */
    void receive(ITask<K,D> task);

    K getTaskKey();

    /*
     *  是否支持多次使用
     *  <li>    一次性还是永久性
     */
    boolean isMulti();

    void setIChain(IChain<K,D> chain);

    IChain<K,D> getIChain();

    /*
     *  任务执行完之后，调用这个函数
     *  <li>    任务执行完之后，需要修改状态为不可执行
     *          <li>    否则，每次loop，任务度标记为可执行，会导致无限执行
     *
     *  <li>    其他任务的执行完毕之后，可能会影响都这个任务的属性
     *          <li>    调用这个函数去修改其他task
     */
    void modify();

    /*
     *   任务执行完之后，将数据跑出来
     *   <li>    可以post到其他线程
     *   <li>    如果想要在主线程中去处理数据
     *
     *   <li>    任务执行之后允许有一个额外的操作
     */
    void post();

    /*
     *  用于判断任务的执行情况
     *  <li>    -1  失败
     *  <li>    其他  成功
     */
    int status();
}
