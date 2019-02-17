package com.wsdc.plugin_test.plugin.home.category;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;

import java.util.List;

@APlugin(key="/test/home/category",sign=PluginSign.PROXY,wrapKey = 200)
public class CategoryProxy extends AbstractIProxy<Fragment> {
    public CategoryProxy(IPlugin<Fragment, Integer> plugin, Context context) {
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
