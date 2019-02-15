package com.wsdchigh.plugin_test.plugin.global;

import android.content.Context;
import android.view.View;

import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;

public class GlobalViewHolder extends AbstractIViewHolder<Object> {
    public GlobalViewHolder(IPlugin<Object, Integer> plugin) {
        super(plugin);
    }

    @Override
    protected void clear() {

    }

    @Override
    public View install(Context context, Object o) {
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
