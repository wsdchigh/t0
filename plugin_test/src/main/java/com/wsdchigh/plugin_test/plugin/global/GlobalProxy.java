package com.wsdchigh.plugin_test.plugin.global;

import android.content.Context;

import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;

import java.util.List;

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
