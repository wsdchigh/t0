package com.wsdc.g_a_0;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

import com.wsdc.g_a_0.plugin.IPlugin;

/*
 *  为了统一化
 *  <li>    所有的activity均继承自FragmentActivity
 *  <li>    requestWindowFeature(Window.FEATURE_NO_TITLE);
 *          <li>    去掉title (如果需要title，在布局中定义一个即可)
 *
 *
 *  <li>    为了方便操作，建议activity集成这个Activity
 *          <li>    如果没有要求，只要简单继承即可
 */
public class BaseActivity extends FragmentActivity {
    //  占位10M的内存，用于测试的时候，检测内存是否泄漏   打包的时候删除即可
    //  byte[] testMemory = new byte[1024 * 1024 * 10];
    IPlugin<Activity,Integer> plugin;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(plugin.viewHolder().install(this,this));
    }


    @Override
    public Resources getResources() {
        /*
         *  主app中使用的是内置的Resources
         *  <li>    插件中使用的是代理Resources
         */
        final Resources r0 = plugin.apk().resources();
        if(r0 instanceof ResourceProxy1){
            ResourceProxy1 resources = (ResourceProxy1) plugin.apk().resources();
            if(resources.getLocal() != super.getResources()){
                resources.setLocal(super.getResources());
            }
            return resources;
        }else{
            return super.getResources();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        plugin.uninstall();
    }
}
