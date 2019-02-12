package com.wsdc.g_a_0.plugin.inner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.IProxy;
import com.wsdc.g_a_0.plugin.IViewHolder;
import com.wsdc.g_a_0.router.IRouter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultPlugin<T> implements IPlugin<T,Integer> {
    private Context context;
    private IRouter router;
    private IProxy<T> proxy;
    private IData data;
    private IViewHolder<T> viewHolder;
    private String key;
    private T wrap;
    private Handler handler;

    private List<IPlugin> plugins = new ArrayList<>();
    private Lock lock = new ReentrantLock();

    private IPlugin parent;

    public DefaultPlugin(Context context, IRouter router,
                         IProxy<T> proxy, IData data, IViewHolder<T> viewHolder,
                         String key, T wrap) {
        this.context = context;
        this.router = router;
        this.proxy = proxy;
        this.data = data;
        this.viewHolder = viewHolder;
        this.key = key;
        this.wrap = wrap;

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                lock.lock();
                try{
                    DefaultPlugin.this.handleMessage(msg,DefaultPlugin.this);
                }finally {
                    lock.unlock();
                }
            }
        };
    }
    public DefaultPlugin(Context context, IRouter router,
                         IProxy<T> proxy, IData data, IViewHolder<T> viewHolder,
                         String key, T wrap,IPlugin parent) {
        this.context = context;
        this.router = router;
        this.proxy = proxy;
        this.data = data;
        this.viewHolder = viewHolder;
        this.key = key;
        this.wrap = wrap;
        this.parent = parent;
        this.handler = parent.handler();

        parent.register(this);
    }

    @Override
    public boolean handleMessage(Message msg,IPlugin plugin0){
        if(plugin0.proxy().containKey(msg.what)){
            plugin0.proxy().proxy(msg.what,msg.obj);
            return true;
        }

        List<IPlugin> children = plugin0.children();
        if(children!= null && children.size() > 0){
            for (IPlugin child : children) {
                if(child.handleMessage(msg,child)){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public IData data() {
        return data;
    }

    @Override
    public IProxy proxy() {
        return proxy;
    }

    @Override
    public IViewHolder<T> viewHolder() {
        return viewHolder;
    }

    @Override
    public IRouter router() {
        return router;
    }

    @Override
    public T wrap() {
        return wrap;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public IPlugin current() {
        if(plugins.size() == 0){
            return null;
        }

        lock.lock();
        final IPlugin plugin = plugins.get(plugins.size()-1);
        lock.unlock();

        return plugin;
    }

    @Override
    public Handler handler() {
        return handler;
    }

    @Override
    public IPlugin parent() {
        return parent;
    }

    @Override
    public List<IPlugin> children() {
        return plugins;
    }

    @Override
    public IPlugin child() {
        return null;
    }

    @Override
    public void register(IPlugin iPlugin) {
        lock.lock();
        if(!plugins.contains(iPlugin)){
            plugins.add(iPlugin);
        }
        lock.unlock();
    }

    @Override
    public void unregister(IPlugin iPlugin) {
        lock.lock();
        plugins.remove(iPlugin);
        lock.unlock();
    }

    @Override
    public void notify0(int type, Integer key) {

    }

}
