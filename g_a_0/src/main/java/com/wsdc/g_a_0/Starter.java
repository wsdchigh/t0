package com.wsdc.g_a_0;

/*
 *  启动者
 *  <li>    通过一系列的配置文件启动，配置文件存放在 /file/starter_wsdc目录下面
 *  <li>    需要在assets中存放一份最简单的配置文件
 *          <li>    如果目录不存在，会在assets中复制一份过来
 *
 *  <li>    配置启动业的相关数据
 *  <li>    配置首页的相关数据
 *
 *  <li>    配置插件的相关信息
 *          <li>    插件的key
 *          <li>    插件的路径
 *          <li>    proxy的路径
 *          <li>    viewHolder的路径
 *          <li>    IData的路径
 *          <li>    IData是否使用parent的IData对象 (不独立创建)
 *          <li>    apk文件的位置
 *          <li>    apk的版本信息
 *                  <li>    需要和服务端进行比对，不同则需要下载
 *
 *          <li>    classLoader的路径
 *          <li>    服务的类型   (Activity?Fragment)
 *                  <li>    不同的插件，需要的Activity可能有一点差别
 *                  <li>    这里使用一个Integer去保存
 *
 *  <li>    Activity配置信息
 *          <li>    内置的Activity (因为必须在注册文件中注册)
 *          <li>    activity的路径
 *          <li>    对应的key  (不同的activity的key可以相同，相同视为一组)
 *
 *  <li>    Service配置信息 (必须在清单文件中注册注册)
 *          <li>    配置一些服务在里面
 *
 *  <li>    广播的注册
 *          <li>    不是必须    广播可以动态注册
 *
 *  <li>    配置一个初始的自动路由
 *          <li>    启动页 ->  那个插件
 */
public class Starter{
    /*
     *  服务端下发的apk配置信息
     *  <li>    版本
     *  <li>    里面保留最近3个版本的的apk信息
     *          <li>    可以根据里面的信息，删除失效的apk包
     *
     *  <li>    里面保留最近3个版本的配置信息
     */
    private String apk_json = "apk.json";

    /*
     *  存放apk的位置
     */
    private String apk_files = "apks";

}
