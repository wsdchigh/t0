package com.wsdc.g_a_0.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

import com.wsdc.g_a_0.ResourceProxy1;
import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouter;

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
            Log.d("wsdc", "获取等级2插件·");
        }else{
            plugin = plugin0;
            Log.d("wsdc", "获取等级1 插件");
        }
        Log.d("wsdc", "key in activity = "+plugin0.key());
        plugin.install(this,-1,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(plugin.viewHolder().install(this,this,null));
        plugin.viewHolder().init(this);
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
    public void onBackPressed() {
        IRouter router = Starter.getInstance().getRouter();

        if(router.size() <= 1){
            /*
             *  如果插件根本没有添加到路由表中
             *  <li>    当前插件并没有在路由表中   == 0
             *
             *  如果插件在路由表中，当前插件是最后一个插件
             *  <li>    == 1
             *
             *  上述两种情况已经无法在继续后退了
             *  <li>    此时路由的back函数会清空路由中的所有数据
             *  <li>    此时可以提供一个在按一次退出的功能
             */
            router.back();
            super.onBackPressed();
            return;
        }else{
            /*
             *  如果只是fragment的切换，返回不会为null
             *  <li>    如果是最后一个fragment，需要切换到其他的Activity   == null
             */
            IPlugin plugin0 = router.back();
            if(plugin0 == null){
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        IPlugin globalPlugin = Starter.getInstance().globalPlugin();
        plugin.data().unregister(plugin.viewHolder());
        if(globalPlugin != null){
            globalPlugin.data().unregister(plugin.viewHolder());
        }
    }
}
