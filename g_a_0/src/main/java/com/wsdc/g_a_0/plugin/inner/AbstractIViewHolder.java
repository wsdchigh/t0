package com.wsdc.g_a_0.plugin.inner;

import android.content.Context;
import android.view.View;

import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.IProxy;
import com.wsdc.g_a_0.plugin.IViewHolder;

public abstract class AbstractIViewHolder<T> implements IViewHolder<T> {
    public Context context;
    public T wrap;
    public IPlugin<T> plugin;
    public View rootView;

    public AbstractIViewHolder(Context context, T wrap,IPlugin<T> plugin) {
        this.context = context;
        this.wrap = wrap;
        this.plugin = plugin;
    }

    @Override
    public IData data() {
        return plugin.data();
    }

    @Override
    public IProxy proxy() {
        return plugin.proxy();
    }

    @Override
    public IPlugin plugin() {
        return plugin;
    }

    @Override
    public T wrap() {
        return wrap;
    }
}
