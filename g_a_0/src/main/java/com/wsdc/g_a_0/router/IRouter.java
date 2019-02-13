package com.wsdc.g_a_0.router;

import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;

/*
 *  路由器
 */
public interface IRouter{
    IRouterMap map();

    IPlugin go(String key,int mode);

    //  后退一步
    IPlugin back();

    //  到主页
    IPlugin home();

    //  存放全局的数据中心
    IData data();

    //  获取当前显示的栈
    IPlugin currentPlugin();

}
