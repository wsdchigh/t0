package com.wsdc.g_a_0;

import android.app.Activity;

import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.IViewHolder;

/*
 *  插件的辅助类
 *  <li>    辅助插件的注册和反注册
 *
 *  <li>    插件的注册需要在View生成之后，避免不必要的异常
 */
public class UPlugin {
    private static final UPlugin instance = new UPlugin();

    public static UPlugin getInstance(){
        return instance;
    }

    public void register(IPlugin plugin){
        IData data0 = plugin.data();
        IViewHolder<Activity> vh0 = plugin.viewHolder();
        IPlugin parentPlugin = (IPlugin) plugin.parent();
        IPlugin globalPlugin = Starter.getInstance().globalPlugin();

        data0.register(vh0);
        if(parentPlugin != null && parentPlugin.data() != data0){
            parentPlugin.data().register(vh0);
        }

        if(globalPlugin != null){
            globalPlugin.data().register(vh0);
        }
    }

    public void unregister(IPlugin plugin){
        IData data0 = plugin.data();
        IViewHolder<Activity> vh0 = plugin.viewHolder();
        IPlugin parentPlugin = (IPlugin) plugin.parent();
        IPlugin globalPlugin = Starter.getInstance().globalPlugin();

        data0.unregister(vh0);
        if(parentPlugin != null && parentPlugin.data() != data0){
            parentPlugin.data().unregister(vh0);
        }

        if(globalPlugin != null){
            globalPlugin.data().unregister(vh0);
        }
    }
}
