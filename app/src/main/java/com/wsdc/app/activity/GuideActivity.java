package com.wsdc.app.activity;

import android.util.Log;

import com.wsdc.g_a_0.base.BaseActivity;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouter;

public class GuideActivity extends BaseActivity {
    @Override
    protected void doWork(IRouter router) {
        super.doWork(router);
        router.check();
        router.go("/test/guide/guide0",IPlugin.START_NOT_STACK | (IPlugin.START_NOT_STACK >> 2));
        Log.d("wsdc1", "hash = "+router.hashCode());
    }
}
