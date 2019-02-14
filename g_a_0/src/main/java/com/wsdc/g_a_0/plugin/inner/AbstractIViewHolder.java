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
    public IPlugin<T,Integer> plugin;
    public View rootView;

    public AbstractIViewHolder(IPlugin<T,Integer> plugin) {
        this.wrap = plugin.wrap();
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

    @Override
    public void uninstall() {
        context = null;
        rootView = null;

        View view = null;

        clear();
    }

    /*
     *   移除跟context相关的资源
     *   <li>    View 持有context
     *   <li>    避免内存泄漏
     */
    protected abstract void clear();
}
