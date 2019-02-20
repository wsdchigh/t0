package com.wsdc.plugin_test.plugin.guide.welcome;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;
import com.wsdc.plugin_test.GK;

import java.util.LinkedList;
import java.util.List;

public class WelcomeProxy extends AbstractIProxy<Fragment> {
    List<Integer> promiseKey = new LinkedList<>();

    {
        promiseKey.add(GK.WELCOME_TO_HOME);
    }
    public WelcomeProxy(IPlugin<Fragment, Integer> plugin, Context context) {
        super(plugin, context);
    }

    @Override
    public boolean proxy0(Integer key, Object... args) {
        switch (key){
            case GK.WELCOME_TO_HOME:
                //plugin().router().check(GK.ROUTE_GUIDE_WELCOME, IPlugin.START_NOT_STACK | (IPlugin.START_NOT_STACK >> 2));
                plugin().router().go(GK.ROUTE_HOME_HOME0,IPlugin.START_COMMON);
                Log.d("wsdc1", "敢问路在何方");

                break;
        }
        return false;
    }

    @Override
    protected List<Integer> keys() {
        return promiseKey;
    }
}
