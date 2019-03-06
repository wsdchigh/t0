package com.wsdc.p_j_0.looper;

/*
 *  如果函数需要轮询，那么实现这个接口
 *  <li>    在loop函数中实现具体的功能
 *  <li>    为了杜绝可能会发送的问题，这里选择抛出Exception
 */
public interface LCall {
    /*
     *  异常出现的位置
     *  <li>    start   异常出现在注册表时候
     *  <li>    loop    异常出现在轮询的时候
     *  <li>    end     异常出现在移除注册的时候
     *
     *  通常异常时出现在loop阶段，比如网络断开
     */
    public static final int EXCEPTION_START = 0;
    public static final int EXCEPTION_LOOP = 1;
    public static final int EXCEPTION_END = 2;
    /*
     *  如果当前实例正式的移动到了正式队列之中
     *  <li>    会发送一次该消息
     *  <li>    注册只是将当前实例发送到注册队列之中，该函数只会在移动到正式IO队列中才会触发
     *
     *  <li>    可以执行一系列的初始化操作
     */
    void toQueue() throws Exception;

    /*
     *  如果实例一旦添加到IO队列中，那么会持续调用loop函数
     */
    boolean loop() throws Exception;

    /*
     *  如果在轮询的过程中出现了异常
     *  <li>    IO线程中回调该函数
     *  <li>    status  不同的周期出现的异常
     *          <li>    0       正式开始
     *          <li>    1       loop阶段
     *          <li>    2       正式退出
     */
    void exception(int status, Exception e);

    /*
     *  主动请求退出轮询
     *  <li>    实例添加在注册队列之中
     *  <li>    不会立即退出，正式退出的时候回调用exitQueue函数
     */
    void requestExit();

    /*
     *  looper正式从IO线程中移除实例，回调这个函数
     *  <li>    在这里对数据进行处理
     *  <li>    和toQueue对应
     */
    void exitQueue() throws Exception;

}
