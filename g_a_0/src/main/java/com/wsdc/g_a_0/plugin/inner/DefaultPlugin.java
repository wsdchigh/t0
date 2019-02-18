package com.wsdc.g_a_0.plugin.inner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wsdc.g_a_0.APK;
import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.XInfo;
import com.wsdc.g_a_0.XInfoAll;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.IProxy;
import com.wsdc.g_a_0.plugin.IViewHolder;
import com.wsdc.g_a_0.router.IRouter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dalvik.system.BaseDexClassLoader;

/*
 *  插件的key
 *  <li>    模块 + 路由地址
 *
 *  <li>    指定的key有特殊含义
 *          <li>    /global/global_plugin       全局的插件
 *                  <li>    这个插件，全局有效
 *
 *          <li>    插件名字可以任意指定，可以是任意apk
 *                  我们需要一个全局的插件处理全局数据
 *                  <li>    在解析的时候定义好即可
 */
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

    private IPlugin currentChild;
    private Map<String,Object> tagMap = new TreeMap<>();
    private int childContainerID;

    private XInfo.WrapInfo wInfo;

    /*
     *  创建顶级插件
     *  <li>    非路由插件也是使用这个函数构建
     */
    public DefaultPlugin(IRouter router,String key, APK apk,XInfoAll infoAll) {
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

        //  创建实例    (Fragment)
        List<XInfo.XPlugin> plugins = apk.info().plugins;
        XInfo.XPlugin plugin0 = null;
        for (XInfo.XPlugin plugin : plugins) {
            if(plugin.key.equals(key)){
                plugin0 = plugin;
                break;
            }
        }

        List<XInfo.WrapInfo> wrapInfos = infoAll.wrapInfos;
        XInfo.WrapInfo wrapInfo0 = null;
        for (XInfo.WrapInfo wrapInfo : wrapInfos) {
            if(wrapInfo.wrapKey == plugin0.wrapKey){
                wrapInfo0 = wrapInfo;
            }
        }
        wInfo = wrapInfo0;
        setTag("wrapInfo",wInfo);

        childContainerID = plugin0.fragmentContainerID;

        //  一级插件是Activity，由系统去创建，我们不去创建
        //  wrap 通过install去获取
    }

    /*
     *  二级插件的创建
     *  <li>    Fragment
     *  <li>    Fragment的切换需要预先构建Fragment，所以，这里需要创建wrap
     *
     */
    public DefaultPlugin(IRouter router, String key, APK apk, IPlugin parent, XInfoAll infoAll) {
        this.router = router;
        this.key = key;
        this.parent = parent;
        this.handler = parent.handler();
        this.apk = apk;
        parent.register(this);
        status = status | 2;

        //  创建实例    (Fragment)
        List<XInfo.XPlugin> plugins = apk.info().plugins;
        XInfo.XPlugin plugin0 = null;
        for (XInfo.XPlugin plugin : plugins) {
            if(plugin.key.equals(key)){
                plugin0 = plugin;
                break;
            }
        }
        List<XInfo.WrapInfo> wrapInfos = infoAll.wrapInfos;
        XInfo.WrapInfo wrapInfo0 = null;
        for (XInfo.WrapInfo wrapInfo : wrapInfos) {
            Log.d("wsdc", "wrap = "+wrapInfo.wrapKey+"      plugin = "+plugin0.wrapKey);
            if(wrapInfo.wrapKey == plugin0.wrapKey){
                wrapInfo0 = wrapInfo;
            }
        }

        Log.d("wsdc", "wrapInfo == null = "+(wrapInfo0 == null)+"   key = ");
        wInfo = wrapInfo0;
        setTag("wrapInfo",wInfo);

        //  二级路由 Fragment 根据配置文件创建一个
        BaseDexClassLoader classLoader = apk.classLoader();
        try {
            /*
             *  只要类型匹对
             *  <li>    无参的构造函数
             */
            Class<?> clz = classLoader.loadClass(wrapInfo0.path);
            wrap = (T) clz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
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
    public void updateStatus(int newStatus) {
        this.status = newStatus;
    }

    @Override
    public int id() {
        return container_id;
    }

    @Override
    public int childLayout() {
        return childContainerID;
    }

    @Override
    public APK apk() {
        return apk;
    }

    @Override
    public void setTag(String key, Object value) {
        tagMap.put(key,value);
    }

    @Override
    public Object getTag(String key) {
        return tagMap.get(key);
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
        //
        if((status & STATUS_INSTALL_MASK) == STATUS_INSTALL_YES){
            return;
        }

        this.context = context;
        this.container_id = container_id;
        if(this.wrap == null){
            this.wrap = t;
        }

        /*
         *  生成 proxy data viewHolder信息
         */
        Log.d("wsdc", "apk == null ?"+(apk == null));
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

        final BaseDexClassLoader classLoader = apk.classLoader();
        try {
            Class proxyClz = classLoader.loadClass(plugin0.proxyPath);
            Class viewHolderClz = classLoader.loadClass(plugin0.viewHolderPath);

            Constructor proxyConstructor = proxyClz.getConstructor(IPlugin.class, Context.class);
            proxyConstructor.setAccessible(true);
            proxy = (IProxy<T>) proxyConstructor.newInstance(this,context);

            Constructor viewHolderConstructor = viewHolderClz.getConstructor(IPlugin.class);
            viewHolderConstructor.setAccessible(true);
            viewHolder = (IViewHolder<T>) viewHolderConstructor.newInstance(this);

            if(parent != null && plugin0.userParent){
                data = parent.data();
            }else{
                Class dataClz = classLoader.loadClass(plugin0.iDataPath);
                Constructor dataConstructor = dataClz.getConstructor(IPlugin.class);
                dataConstructor.setAccessible(true);
                data = (IData) dataConstructor.newInstance(this);
            }

            if(proxy == null){
                throw new RuntimeException("proxy == null");
            }

            if(viewHolder == null){
                throw new RuntimeException("viewHolder == null");
            }

            if(data == null){
                throw new RuntimeException("data == null");
            }

            Log.d("wsdc", "hash = "+hashCode());

            //  数据中心注册观察者
            data.register(viewHolder);

            /*
             *  如果有父插件
             *  <li>    应该需要监听父插件的数据转换
             */
            if(parent != null){
                if(data != parent.data()){
                    Log.d("wsdc", "parent hash = "+parent.hashCode()+"  self key = "+hashCode()+"  parent key = "+parent.key());
                    Log.d("wsdc", "parent = "+(parent == null)+"    data = "+(parent.data() == null));
                    parent.data().register(viewHolder);
                }
            }

            /*
             *  全局插件的注册信息
             *  <li>    任何插件均自动注册到全局插件的数据中心中
             *  <li>    构建全局插件的时候，是无法获取全局插件的
             */
            if(Starter.getInstance() != null){
                final IPlugin globalPlugin = Starter.getInstance().globalPlugin();
                if(globalPlugin != null){
                    globalPlugin.data().register(viewHolder);
                }
            }
            return ;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("wsdc", "exception = "+e.getClass().getName()+"   msg = "+e.getMessage());
        }

        throw new RuntimeException("插件安装异常");
    }

    @Override
    public void uninstall() {

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
        return currentChild;
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
        currentChild = iPlugin;
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
