package com.wsdc.g_a_0;

import android.app.Application;

import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouter;

/*
 *  可以继承这个Application
 *  <li>    可以直接使用
 *  <li>    可以复制里面的代码到自己的Application之中
 *
 *
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
        router.go("/test/t0",IPlugin.START_COMMON | (IPlugin.START_COMMON >> 2));

    }
}
