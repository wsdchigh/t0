package com.wsdc.g_a_0.router;

import com.wsdc.g_a_0.APK;
import com.wsdc.g_a_0.XInfoAll;
import com.wsdc.g_a_0.plugin.IPlugin;

import java.util.Map;

/*
 *  路由表
 */
public interface IRouterMap {
    /*
     *  插件获取
     *  <li>    key 所有的路由通过key识别    (唯一)
     *          <li>    key可以用于路由识别
     *          <li>    不是所有的key均用于路由
     *  <li>    如果上层的插件不存在，这里会创建上层的插件
     *
     *  <li>    mode    启动参数
     */
    IPlugin getRouterPlugin(String key, int mode);


    /*
     *  如果插件不参与路由
     *  <li>    使用该函数获取
     *  <li>    均只是一级插件
     *  <li>
     */
    IPlugin getNormalPlugin(String key);

    Map<String,APK> apkMap();

    XInfoAll infoAll();
}
