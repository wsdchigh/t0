package com.wsdc.plugin_test.plugin.home;

import android.app.Activity;
import android.content.Context;

import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;
import com.wsdc.g_a_0.router.IRouter;
import com.wsdc.plugin_test.GK;
import com.wsdc.plugin_test.R;

import java.util.LinkedList;
import java.util.List;

@APlugin(key="/test/home",sign=PluginSign.PROXY,wrapKey = 104,fragmentContainerID = R.id.test_home_act_container)
public class HomeProxy extends AbstractIProxy<Activity> {
    private List<Integer> keys = new LinkedList<>();
    IRouter router = Starter.getInstance().getRouter();

    {
        keys.add(GK.HOME_TO_HOME0);
        keys.add(GK.HOME_TO_CATEGORY);
        keys.add(GK.HOME_TO_CART);
        keys.add(GK.HOME_TO_USER);
        keys.add(GK.BACK_PRESS);
    }

    public HomeProxy(IPlugin<Activity, Integer> plugin, Context context) {
        super(plugin, context);
    }

    @Override
    public boolean proxy0(Integer key, Object... args) {
        switch (key){
            case GK.HOME_TO_HOME0:
                router.go(GK.ROUTE_HOME_HOME0,IPlugin.START_COMMON);
                break;

            case GK.HOME_TO_CATEGORY:
                router.go(GK.ROUTE_HOME_CATEGORY,IPlugin.START_COMMON);

                break;

            case GK.HOME_TO_CART:
                router.go(GK.ROUTE_HOME_CART,IPlugin.START_COMMON);
                break;

            case GK.HOME_TO_USER:
                router.go(GK.ROUTE_HOME_USER,IPlugin.START_COMMON);
                break;

            case GK.BACK_PRESS:
                
                break;
        }
        return false;
    }

    @Override
    protected List<Integer> keys() {
        return keys;
    }
}
