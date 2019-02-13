package com.wsdc.g_a_0.router;

import com.wsdc.g_a_0.plugin.IPlugin;

/*
 *  路由表
 */
public interface IRouterMap {
    /*
     *  插件获取
     *  <li>    key 所有的路由通过key识别    (唯一)
     *  <li>    如果上层的插件不存在，这里会创建上层的插件
     *
     *  <li>    mode    启动参数
     */
    IPlugin get(String key,int mode);
}
