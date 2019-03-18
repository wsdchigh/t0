package com.wsdc.g_a_0;

import android.content.res.Resources;

import dalvik.system.BaseDexClassLoader;

/*
 *  记录任何一个APK的配置
 *  <li>    ClassLoader
 *  <li>    Resources
 *
 *  <li>    考虑到apk的体积比较大，或者比较多
 *          <li>    考虑同步加载和异步加载
 */
public interface APK {
    /*
     *  提供一个ClassLoader，加载apk中的数据
     */
    BaseDexClassLoader classLoader();

    /*
     *  提供一个资源管理者，用于管理apk中的资源
     */
    Resources resources();

    /*
     *  获取APK中携带的信息
     */
    XInfo info();

    /*
     *   获取所有的插件信息
     */
    //XInfoAll infoAll();

    /*
     *  是否加载
     *  <li>    如果apk没有加载，那么此时是无法加载插件的
     */
    boolean hasLoading();

    /*
     *  加载
     *  <li>    同步，加载的时候回立即加载
     *  <li>    异步，使用一个异步线程去执行加载任务
     *          完成的时候回标记loading
     */
    void loading() throws Exception;
}
