package com.wsdc.g_a_0;

import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/*
 *  记录任何一个APK的配置
 *  <li>    ClassLoader
 *  <li>    Resources
 */
public interface APK {
    /*
     *  提供一个ClassLoader，加载apk中的数据
     */
    DexClassLoader classLoader();

    /*
     *  提供一个资源管理者，用于管理apk中的资源
     */
    Resources resources();
}
