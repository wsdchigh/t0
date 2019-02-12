package com.wsdc.g_a_0.plugin;

/*
 *  数据中心的通知
 */
public interface IDataChangeListener<T,K> {
    void notify0(int type,K key,T t);
}
