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
    IPlugin back();

    //  到主页
    IPlugin home();

    /*
     *  如果需要主动关闭插件
     *  <li>    go函数工作的时候，如果需要关闭前一个插件   (前一个插件不添加到路由中)
     *  <li>    因为一级插件重复
     *          <li>    下面均有二级插件
     *          <li>    此时，需要关闭之前的一级插件
     *          <li>    如果一级插件独立工作，那么是允许重复的  只有在一级插件充当容器的时候，只能存在一个
     */
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

}
