package com.wsdc.g_a_0.router;

import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;

/*
 *  路由器
 */
public interface IRouter{
    /*
     *  路由的添加信息
     *  <li>    common      添加到路由表中，不做任何检查
     *  <li>    single      添加之前，检测路由是否存在 如果存在
     */
    public static final int ROUTER_COMMON = 1;
    public static final int ROUTER_SINGLE = 2;
    public static final int ROUTER_NOT_STACK = 3;
    public static final int ROUTER_NEW_STACK = 4;

    IRouterMap map();

    IPlugin go(String key,int type);

    //  后退一步
    IPlugin back();

    //  到主页
    IPlugin home();

    //  存放全局的数据中心
    IData data();

    //  获取当前显示的栈
    IPlugin currentPlugin();

}
