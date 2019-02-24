package com.wsdc.plugin_test.plugin.global;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;

@APlugin(key="/test/global_main",sign=PluginSign.VIEW_HOLDER)
public class GlobalViewHolder extends AbstractIViewHolder<Object> {
    public GlobalViewHolder(IPlugin<Object, Integer> plugin) {
        super(plugin);
    }

    @Override
    public View install(Context context, Object o, ViewGroup parent) {
        return null;
    }

    @Override
    public void notify0(int type, Integer key, IData iData) {

    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void exit(Context context) {

    }
}
