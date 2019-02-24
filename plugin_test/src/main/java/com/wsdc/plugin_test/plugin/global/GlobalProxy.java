package com.wsdc.plugin_test.plugin.global;

import android.content.Context;

import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;
import com.wsdc.g_a_0.router.IRouter;
import com.wsdc.plugin_test.GK;

import java.util.LinkedList;
import java.util.List;

@APlugin(key="/test/global_main",sign=PluginSign.PROXY,wrapKey = 400)
public class GlobalProxy extends AbstractIProxy<Object> {
    private List<Integer> keys = new LinkedList<>();

    {
        keys.add(GK.GUIDE0_GO);
        keys.add(GK.SYSTEM_START);
    }

    public GlobalProxy(IPlugin<Object, Integer> plugin, Context context) {
        super(plugin, context);
    }

    @Override
    public boolean proxy0(Integer key, Object... args) {
        switch (key){
            case GK.SYSTEM_START:

                break;

            case GK.GUIDE0_GO:
                go0();
                break;
        }
        return false;
    }

    private void go0() {
        plugin().handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IRouter router = Starter.getInstance().getRouter();
                if(router != null){
                    //router.go(GK.ROUTE_GUIDE_WELCOME, IPlugin.START_NOT_STACK | (IPlugin.START_NOT_STACK >> 2));
                   router.go(GK.ROUTE_HOME_HOME0, IPlugin.START_COMMON);
                }
            }
        },3000);
    }

    @Override
    protected List<Integer> keys() {
        return keys;
    }
}
