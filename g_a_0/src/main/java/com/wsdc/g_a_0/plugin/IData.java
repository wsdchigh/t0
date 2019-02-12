package com.wsdc.g_a_0.plugin;

import com.wsdc.g_j_0.IContainer0;

import java.util.List;
import java.util.Map;

/*
 *  数据中心
 *  <li>    最好有一个全局的数据中心
 *  <li>    不同的插件可以共用一个IData
 */
public interface IData<K> extends IContainer0<IDataChangeListener,K>{
    void put(K key,Object value);
    void put(Map<K,Object> values);

    Object get(K key);

    List<Object> get(List<K> keys);

    IPlugin plugin();
}
