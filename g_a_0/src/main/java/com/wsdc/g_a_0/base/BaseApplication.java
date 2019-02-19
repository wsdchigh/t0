package com.wsdc.g_a_0.base;

import android.app.Application;
import android.util.Log;

import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.router.IRouter;

/*
 *  可以继承这个Application
 *  <li>    可以直接使用
 *  <li>    可以复制里面的代码到自己的Application之中
 *
 *  <li>    application需要注意事项
 *          <li>    正常的退出app，并不会销毁application
 *                  整个application持有的是空的activity栈，但是在后台管理界面，是能够看到当前的app的
 *
 *          <li>    所以，退出app，再次进入app，不一定会重新创建application    (因为各种原因，是可能创建的，比如很长时间没有启动)
 *          <li>    所以，关于activity的相关信息，不要存储在application之中
 *
 *          <li>    需要自己去在代码中检测app的退出   (数据的销毁)
 *                  需要自己在代码中初始化app         (数据的重新搭建)
 *
 *                  <li>    这里保留路由的引用，不要有路由的相关操作
 *                  <li>    需要在其他位置控制路由数据的清空
 */
public class BaseApplication extends Application {
    Starter starter;
    IRouter router;

    @Override
    public void onCreate() {
        super.onCreate();
        Starter.install(this);
        starter = Starter.getInstance();
        router = starter.getRouter();
        //  router.go("/test/guide/guide0",IPlugin.START_NOT_STACK | (IPlugin.START_NOT_STACK >> 2));

        Log.d("wsdc1", "创建application");
    }
}
