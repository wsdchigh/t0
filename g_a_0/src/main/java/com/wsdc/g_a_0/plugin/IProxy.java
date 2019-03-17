package com.wsdc.g_a_0.plugin;

/*
 *
 *  <li>    里面注册了字符串类型的key
 *          <li>    如果需要处理，必须要有可以
 *          <li>    如果没有key，会将任务抛给上层的IProxy 进行处理
 */
public interface IProxy<T>{
    boolean containKey(Integer key);

    IPlugin<T,Integer> plugin();

    //  具体的处理函数  因为参数的不确定，所以使用可变参数
    boolean proxy(Integer key,Object... args);
}
