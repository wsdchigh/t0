package com.wsdc.plugin_test.plugin.global;

import android.content.Context;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;

import java.util.List;

@APlugin(key="/test/global_main",sign=PluginSign.PROXY,wrapKey = 400)
public class GlobalProxy extends AbstractIProxy<Object> {
    public GlobalProxy(IPlugin<Object, Integer> plugin, Context context) {
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
