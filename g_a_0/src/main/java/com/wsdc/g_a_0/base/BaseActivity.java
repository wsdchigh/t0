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
 *
 *
 *  <li>    工作责任
 *          <li>    create的时候注册插件
 *          <li>    destroy的时候取消注册插件
 *
 *          <li>    create的时候持有插件   (初始化好插件)
 *
 *  <li>    关于全局插件引导
 *          <li>    运行时权限的申请
 *          <li>    文件权限的申请
 *          <li>    服务的开启
 *          <li>    全局的数据引导 (登录的用户)
 *          <li>    全局的缓存
 */
public class BaseActivity extends FragmentActivity {
    //  占位10M的内存，用于测试的时候，检测内存是否泄漏   打包的时候删除即可
    //  byte[] testMemory = new byte[1024 * 1024 * 10];
    IPlugin<Activity,Integer> plugin;
    IRouter router = Starter.getInstance().getRouter();

    {
        doWork(router);
        IPlugin plugin0 = router.currentPlugin();
        if((plugin0.status() & IPlugin.STATUS_LEVEL_MASK) == 2){
            plugin = (IPlugin<Activity, Integer>) plugin0.parent();
        }else{
            plugin = plugin0;
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
        Log.d("wsdc1", "activity create");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wsdc1", "activity start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("wsdc1", "activity restart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        IRouter router = Starter.getInstance().getRouter();
        Log.d("wsdc1", "base resume path = "+this.getClass().getName()+"    router size = "+router.size()+"     path = "+router.currentPlugin().key());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wsdc1", "activity pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wsdc1", "activity stop");
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
        int rtn = router.back0();
        Log.d("wsdc1", "back = "+rtn);

        if(rtn != IRouter.ROUTER_BACK_FRAGMENT){
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        IPlugin globalPlugin = Starter.getInstance().globalPlugin();
        plugin.data().unregister(plugin.viewHolder());
        if(globalPlugin != null){
            globalPlugin.data().unregister(plugin.viewHolder());
        }
        super.onDestroy();
    }

    /*
     *  暴露这个函数
     *  <li>    如果路由是空的
     */
    protected void doWork(IRouter router){

    }
}
