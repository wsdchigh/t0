package com.wsdc.g_j_0;

/*
 *  观察者注册接口
 */
public interface IContainer0<T,K> {
    /*
     *  notify0会调用的type
     *  <li>    init    接受初始化的通知
     *  <li>    single  接受单个key信号
     *  <li>    current 只有当前显示的界面才能接受到通知
     */
    public static final int TYPE_INIT = 1;
    public static final int TYPE_SINGLE = 2;
    public static final int TYPE_CURRENT = 2;

    void register(T t);
    void unregister(T t);

    void notify0(int type,K key);
}
