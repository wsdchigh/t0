package com.wsdc.plugin_test.plugin.home;

import android.app.Activity;
import android.content.Context;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;
import com.wsdc.plugin_test.R;

import java.util.List;

@APlugin(key="/test/home",sign=PluginSign.PROXY,wrapKey = 104,fragmentContainerID = R.id.test_home_act_container)
public class HomeProxy extends AbstractIProxy<Activity> {
    public HomeProxy(IPlugin<Activity, Integer> plugin, Context context) {
        super(plugin, context);
    }

    @Override
    public boolean proxy0(Integer key, Object... args) {
        return false;
    }

    @Override
    protected List<Integer> keys() {
        return null;
    }
}
