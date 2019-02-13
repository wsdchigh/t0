package com.wsdc.g_a_0.router;

import com.wsdc.g_a_0.XInfoAll;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;

/*
 *  路由器
 *  <li>    路由通过一个字符串识别
 *          <li>    /module_name/routerA/routerB
 *          <li>    声明模块，快速定位
 *          <li>    routerA 一级路由
 *          <li>    routerB 二级路由
 */
public interface IRouter{
    IRouterMap map();

    IPlugin go(String key,int mode);

    //  后退一步
    IPlugin back();

    //  到主页
    IPlugin home();

    /*
     *  当前路由中，是否存在对应的一级路由
     *  <li>    如果有，则返回
     *  <li>    如果没有，返回null
     */
    IPlugin getExistsPluginLevel1(String key);

    //  存放全局的数据中心
    IData data();

    //  获取当前显示的栈
    IPlugin currentPlugin();

    //  获取所有的插件信息
    XInfoAll infoAll();

}
