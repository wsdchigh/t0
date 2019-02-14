package com.wsdchigh.plugin_test;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.wsdc.g_a_0.ResourceProxy1;
import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.IProxy;
import com.wsdc.g_a_0.plugin.IViewHolder;

public class MainActivity extends FragmentActivity {
    IPlugin<Activity,Integer> plugin;
    private IProxy proxy;
    private IData data;
    private IViewHolder<Activity> viewHolder;

    {
        IPlugin plugin0 = Starter.getInstance().getRouter().currentPlugin();
        if((plugin0.status() & IPlugin.STATUS_LEVEL_MASK) == 2){
            plugin = (IPlugin<Activity, Integer>) plugin0.parent();
        }else{
            plugin = plugin0;
        }
        plugin.install(this,-1,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(plugin.viewHolder().install(this,this));

        //  获取插件信息
        proxy = plugin.proxy();
        data = plugin.data();
        viewHolder = plugin.viewHolder();

        //  数据中心注册观察者
        data.register(viewHolder);
    }


    @Override
    public Resources getResources() {
        ResourceProxy1 resources = (ResourceProxy1) plugin.apk().resources();
        if(resources.getLocal() != super.getResources()){
            resources.setLocal(super.getResources());
        }
        return resources;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //  数据中心取消注册观察者
        data.unregister(viewHolder);
    }
}
