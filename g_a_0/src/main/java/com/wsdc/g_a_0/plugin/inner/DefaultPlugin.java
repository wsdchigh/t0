package com.wsdc.g_a_0.plugin.inner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.wsdc.g_a_0.APK;
import com.wsdc.g_a_0.XInfo;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.IProxy;
import com.wsdc.g_a_0.plugin.IViewHolder;
import com.wsdc.g_a_0.router.IRouter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dalvik.system.DexClassLoader;

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

    private int status = 0;
    private int container_id;
    private APK apk;

    //  创建顶层的插件
    public DefaultPlugin(IRouter router,String key, APK apk) {
        this.router = router;
        this.key = key;
        this.apk = apk;

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

        status = status | 1;
    }

    /*
     *  二级插件的创建
     *  <li>    Fragment
     *  <li>    Fragment的切换需要预先构建Fragment，所以，这里需要创建wrap
     */
    public DefaultPlugin(IRouter router,String key,APK apk,IPlugin parent) {
        this.router = router;
        this.key = key;
        this.parent = parent;
        this.handler = parent.handler();
        this.apk = apk;

        parent.register(this);

        status = status | ((parent.status() & STATUS_LEVEL) + 1);

        XInfo info = apk.info();
        XInfo.XPlugin plugin0 = null;
        info.plugins
        for (XInfo.WrapInfo wrapInfo : info.wrapInfos) {

        }
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
    public int status() {
        return status;
    }

    @Override
    public int id() {
        return container_id;
    }

    @Override
    public APK apk() {
        return apk;
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
    public void install(Context context,int container_id,T t) {
        this.context = context;
        this.container_id = container_id;
        this.wrap = t;

        /*
         *  生成 proxy data viewHolder信息
         */
        List<XInfo.XPlugin> plugins = apk.info().plugins;
        XInfo.XPlugin plugin0 = null;
        for (XInfo.XPlugin plugin : plugins) {
            if(plugin.key.equals(key)){
                plugin0 = plugin;
                break;
            }
        }

        if(plugin0 == null){
            throw  new RuntimeException("不存在指定的插件");
        }

        final DexClassLoader classLoader = apk.classLoader();
        try {
            Class proxyClz = classLoader.loadClass(plugin0.proxyPath);
            Class viewHolderClz = classLoader.loadClass(plugin0.viewHolderPath);

            Constructor proxyConstructor = proxyClz.getConstructor(IPlugin.class, Context.class);
            proxyConstructor.setAccessible(true);
            proxy = (IProxy<T>) proxyConstructor.newInstance(this,context);

            Constructor viewHolderConstructor = viewHolderClz.getConstructor(IPlugin.class, Context.class);
            viewHolderConstructor.setAccessible(true);
            viewHolder = (IViewHolder<T>) viewHolderConstructor.newInstance(this,context);

            if(parent != null && plugin0.userParent){
                data = parent.data();
            }else{
                Class dataClz = classLoader.loadClass(plugin0.iDataPath);
                Constructor dataConstructor = dataClz.getConstructor(IPlugin.class);
                dataConstructor.setAccessible(true);
                data = (IData) dataConstructor.newInstance(this);
            }

            return ;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("插件安装异常");
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
