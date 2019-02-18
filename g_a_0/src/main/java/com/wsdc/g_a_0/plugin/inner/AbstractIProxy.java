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

    /*
     *  构造函数固定
     *  <li>    按照这个函数进行反射获取
     */
    public AbstractIProxy(IPlugin<T,Integer> plugin, Context context) {
        this.keys = keys();
        this.plugin = plugin;
        this.context = context;

        wrap = plugin.wrap();
    }

    @Override
    public boolean containKey(Integer key) {
        if(keys == null){
            keys = keys();
        }
        return keys.contains(key);
    }

    @Override
    public IPlugin<T,Integer> plugin() {
        return plugin;
    }

    @Override
    public boolean proxy(Integer key, Object... args) {
        boolean rtn = false;

        /*
         *  如果当前proxy支持指定key的处理
         *  <li>    返回值不用来是否处理，而是有些函数需要一个boolean类型的返回值
         */
        if(containKey(key)){
            rtn = proxy0(key, args);
            return rtn;
        }
        return plugin().proxy().proxy(key,args);
    }

    public abstract boolean proxy0(Integer key,Object... args);

    protected abstract List<Integer> keys();
}
