package com.wsdc.g_a_0.plugin.inner;

import android.content.Context;

import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.IProxy;

import java.util.List;

public abstract class AbstractIProxy<T> implements IProxy<T> {
    private List<Integer> keys;
    private IPlugin<T,Integer> plugin;
    public Context context;
    public T wrap;

    public AbstractIProxy(List<Integer> keys, IPlugin<T,Integer> plugin, Context context) {
        this.keys = keys;
        this.plugin = plugin;
        this.context = context;

        wrap = plugin.wrap();
    }

    @Override
    public boolean containKey(Integer key) {
        return keys.contains(key);
    }

    @Override
    public IPlugin<T,Integer> plugin() {
        return plugin;
    }

    @Override
    public boolean proxy(Integer key, Object... args) {
        if(containKey(key)){
            return proxy0(key,args);
        }
        return plugin().proxy().proxy(key,args);
    }

    public abstract boolean proxy0(Integer key,Object... args);
}
