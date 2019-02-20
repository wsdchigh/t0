package com.wsdc.plugin_test;

import com.wsdc.g_a_0.base.BaseActivity;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouter;

/*
 *  为了统一化
 *  <li>    所有的activity均继承自FragmentActivity
 *  <li>    requestWindowFeature(Window.FEATURE_NO_TITLE);
 *          <li>    去掉title (如果需要title，在布局中定义一个即可)
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void doWork(IRouter router) {
        super.doWork(router);
        router.check();
        router.go("/test/guide/guide0",IPlugin.START_NOT_STACK | (IPlugin.START_NOT_STACK >> 2));
    }
}
