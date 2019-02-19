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

    /*
     *  路由操作
     *  <li>    mode    需要携带
     */
    IPlugin go(String key,int mode);

    //  后退一步
    @Deprecated
    IPlugin back();


    public static final int ROUTER_BACK_FRAGMENT = 1;
    public static final int ROUTER_BACK_ACTIVITY = 2;
    public static final int ROUTER_BACK_EMPTY = 3;
    /*
     *  后退
     *  <li>    1       activity里面的fragment后退切换
     *  <li>    2       activity的后退切换
     *  <li>    3       已经空栈了
     */
    int back0();

    /*
     *  清空路由表   (非路由插件表)
     *  <li>    重新进去新的Activity
     */
    IPlugin home();

    @Deprecated
    void close(IPlugin plugin);

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

    int size();

    /*
     *  清空跟路由表相关的所有参数
     */
    void clear();

}
