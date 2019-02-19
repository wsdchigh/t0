package com.wsdc.app.activity;

import com.wsdc.g_a_0.base.BaseActivity;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouter;

public class GuideActivity extends BaseActivity {
    @Override
    protected void doWork(IRouter router) {
        super.doWork(router);
        router.go("/test/guide/guide0",IPlugin.STATUS_INSTALL_NOT | (IPlugin.START_NOT_STACK >> 2));
    }
}
