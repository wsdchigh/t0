package com.wsdc.g_a_0;

import com.wsdc.g_a_0.router.IRouterMap;

/*
 *  负责加载APK
 *  <li>   负责APK信息的加载
 *         属于异步操作
 */
public interface IStarter {
    /*
     *  加载配置信息
     *  <li>    如果本地没有存储配置信息，那么从assets中加载默认配置
     *  <li>    如果files目录下面存储了服务器的同步信息
     *
     */
    XInfoAll infoAll();


    /*
     *  一个APK文件有专属的配置文件
     *  <li>    当一个APK文件加载完成之后(成功还是失败),通过Callback返回消息
     */
    APK load(XInfo info) throws Exception;


    /*
     *  和服务器同步信息
     *  <li>    true    表示需要重新启动APP
     *  <li>    通常是需要一系列的http请求来实现，期间可能会出现错误
     *          <li>    一定要抛出异常
     *
     *  <li>    需要实现事务的功能
     */
    boolean toggle() throws Exception;

    /*
     *  具体存储APK，插件，路由的信息表
     *  <li>    属于容器
     */
    IRouterMap routerMap();

    public interface APKLoadingCallback{
        void success(APK apk);

        void failure(APK apk,Exception e);
    }
}
