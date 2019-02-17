package com.wsdc.plugin_test.plugin.guide.welcome;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;

import java.util.List;

public class WelcomeProxy extends AbstractIProxy<Fragment> {
    public WelcomeProxy(IPlugin<Fragment, Integer> plugin, Context context) {
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
