package com.wsdc.g_a_0.plugin;

/*
 *  数据中心的通知
 */
public interface IDataChangeListener<T,K> {
    /*
     *  所有的View均在这里面对数据进行赋值
     *  <li>    其他位置只需要做出初始化即可
     */
    void notify0(int type,K key,T t);
}
