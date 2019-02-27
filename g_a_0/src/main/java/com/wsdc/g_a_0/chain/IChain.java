package com.wsdc.g_a_0.chain;

/*
 *  事件调用链
 *  <li>    任何任务均维系一个key，通常是Integer(不分大小的)
 *
 *  <li>    线程观念
 *          <li>    任务可以在主线程中执行，也可以在子线程中执行
 *          <li>    通常任务在IO线程中执行即可，如果main线程需要，我们才会切换到主线程中来
 *
 *  <li>    任务观念
 *          <li>    任务有没有执行     (执行状态)
 *          <li>    任务执行结果       (成功?失败?异常?还是其他)
 *                  <li>    我们通常是希望任务是成功的，通常是在成功的基础之后才会执行下一步
 *                  <li>    成功的对立面，我们是为else 无论是异常还是错误或者是其他的状态，均调用这个函数
 *
 *                  <li>    函数执行返回之后是有返回值，如果我们针对else，可以注册指定的返回值来执行
 *                  <li>    1   success
 *                  <li>    0   failure
 *                  <li>    -1  exception
 *                  <li>    其他，可以自行定义
 *
 *  <li>    关系表
 *          <li>    单链关系，如果A执行完毕，那么B执行
 *          <li>    多条件关系，如果A执行成功，并且B执行成功，我们才执行C
 *          <li>    或关系     如果A或者B执行成功，我们在执行C
 *          <li>    其他的复杂关系暂时不设定()
 *
 *  <li>    取消
 *          <li>    为了避免内存泄漏，任务是支持取消的(如果任务没有执行)
 *
 *  <li>    周期性
 *          <li>    同样是针对内存问题，提供周期性函数
 *
 *  <li>    泛型
 *          <li>    K   key 任务标识自身的key(建议是Integer，其次是String)
 *          <li>    D   data    这里可以是任意类型，建议是一个容器(Map是最好的),用于存储过程中产生的数据
 *                  <li>    例如，如果这个任务执行成功了，那么将数据存放在Map表中，其他任务通过依赖任务的key取出数据
 *                          实现数据的流通
 *                  <li>    这是系统设计的初衷，对于数据的传递，可以通过其他方式，不一定要遵循系统的设定
 */
public interface IChain<K,D> {

    void doMain(ITask<K,D> task);
    void doIO(ITask<K,D> task);

    void then(ITask<K,D> task);

   void cancel(ITask<K,D> task);

    void start();

    void exit();
}
