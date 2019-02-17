package com.wsdc.plugin_test.plugin.home.home0;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;

import java.util.List;

@APlugin(key="/test/home/home0",sign=PluginSign.PROXY,wrapKey = 200)
public class Home0Proxy extends AbstractIProxy<Fragment> {
    public Home0Proxy(IPlugin<Fragment, Integer> plugin, Context context) {
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
