package com.wsdc.g_a_0.router.inner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.wsdc.g_a_0.RouterUtil;
import com.wsdc.g_a_0.XInfo;
import com.wsdc.g_a_0.XInfoAll;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouter;
import com.wsdc.g_a_0.router.IRouterMap;

import java.util.LinkedList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/*
 *  默认路由实现
 *  <li>    路由走的都是底层的插件
 *          <li>    插件下面不在有子插件
 *
 *  <li>    监听Activity的周期
 *          <li>    在Activity类中的onCreate函数自动通知application，发送信息
 *          <li>    所以关于插件的获取一定要在Activity.onCreate()之前，推荐使用代码块的方式去获取当前的插件信息
 *                  <li>    那么在onCreate()中就可以正常的使用插件了
 *
 *          <li>    也可以选择在回调中去获取插件信息    (后期会考虑这个)
 */
public class DefaultIRouterImpl implements IRouter, Application.ActivityLifecycleCallbacks {
    IRouterMap routerMap ;
    IPlugin currentIPlugin;

    //  记录上一次的插件
    IPlugin lastPlugin;
    IData<Integer> data;

    List<IPlugin> pluginStack = new LinkedList<>();

    /*
     *  二级插件对应的一级插件，不存在于路由之中
     *  <li>    activity创建，并且安装插件
     *  <li>    第一个fragment不添加到路由，导致路由表中无法找到这个一级插件的信息
     *
     *  <li>    第二个fragment如果没有插件安装，那么会导致空指针异常
     *
     *  <li>    所以，针对，没有进入路由的二级路由   提供一个额外的存储
     */
    List<IPlugin> pluginsLevel1NotInStack = new LinkedList<>();
    XInfoAll infoAll;

    Context context;

    public DefaultIRouterImpl(XInfoAll infoAll, Context context) {
        this.infoAll = infoAll;
        routerMap = new DefaultIRouterMapImpl(infoAll,context,this);
        this.context = context;
        ((Application) context).registerActivityLifecycleCallbacks(this);
    }

    @Override
    public IRouterMap map() {
        return routerMap;
    }

    /*
     *  路由规则
     *  <li>    一个Activity视为大的插件
     *  <li>    activity的启动使用普通模式   (为任何插件启动一个Activity)
     *          <li>    但是插件不能重复，如果插件一旦重复，那么会关闭之前的插件
     *                  <li>    所处的activity关闭
     *                  <li>    路由别表相关的插件一律移除
     *
     */
    @Override
    public IPlugin go(String key, int mode) {
        /*
         *  如果系统中存在着这个路由，那么选择复用这个路由
         */
        Log.d("wsdc1", "where to go  = "+key);
        IPlugin plugin = null;
        for (IPlugin plugin1 : pluginStack) {
            if(plugin1.key().equals(key)){
                plugin = plugin1;
                pluginStack.remove(plugin);
                break;
            }
        }

        if(plugin == null){
            plugin = routerMap.getRouterPlugin(key,mode);
        }
        IPlugin origin = currentIPlugin;
        if(plugin != null){
            switch (mode & IPlugin.STATUS_START_SELF_MODE_MASK){
                case IPlugin.START_COMMON:
                    //  添加到栈里面
                    pluginStack.add(plugin);
                    break;

                case IPlugin.START_NOT_STACK:
                    //  不添加到栈里面
                    if(getLevel(plugin) == 2){
                        pluginsLevel1NotInStack.add((IPlugin) plugin.parent());
                    }
                    break;

                default:
                    throw new RuntimeException("不支持的启动模式");
            }
            /*
             *  只有一种情况会触发   (origin)
             *  <li>    APP启动的时候，会等于null
             *          <li>    其余情况一律不会
             *
             *  <li>    只处理Activity之中两个Fragment的处理
             *          <li>    其他一律是两个Activity之间处理
             *          <li>    Activity没有实例化，针对plugin内部的处理，没有多大的操作空间
             *
             *  <li>    比对路由信息
             */
            if(origin == null){

            }else{
                RouterUtil.RouterBean originInfo = RouterUtil.parse(origin.key());
                RouterUtil.RouterBean newInfo = RouterUtil.parse(plugin.key());

                /*
                 *  一个Activity里两个Fragment的切换
                 */
                if(originInfo.level ==2 &&
                        originInfo.level == newInfo.level &&
                        originInfo.module_name.equals(newInfo.module_name) &&
                        originInfo.router_level_1.equals(newInfo.router_level_1)){

                    IPlugin parentPlugin = (IPlugin) origin.parent();
                    FragmentActivity fa = (FragmentActivity) parentPlugin.wrap();

                    Fragment of = (Fragment) origin.wrap();
                    Fragment nf = (Fragment) plugin.wrap();

                    FragmentTransaction transaction = fa.getSupportFragmentManager().beginTransaction();
                    transaction.remove(of)
                            .add(parentPlugin.childLayout(),nf)
                            .show(nf)
                            .commit();

                    Log.d("wsdc1", "go go "+origin.key()+"  ->  "+plugin.key());

                }else{
                    /*
                     *  Activity的跳转
                     */
                    Activity act = null;
                    if(getLevel(origin) == 1){
                        act = (Activity) origin.wrap();
                    }else{
                        IPlugin parentPlugin = (IPlugin) origin.parent();
                        act = (Activity) parentPlugin.wrap();
                    }

                    XInfo.WrapInfo info = null;

                    if(getLevel(plugin) == 1){
                        info = (XInfo.WrapInfo) plugin.getTag("wrapInfo");
                    }else{
                        IPlugin parentPlugin = (IPlugin) plugin.parent();
                        info = (XInfo.WrapInfo) parentPlugin.getTag("wrapInfo");
                    }

                    if(info == null){
                        throw new RuntimeException("info is null ");
                    }

                    Class clz = null;
                    try{
                        BaseDexClassLoader classLoader = plugin.apk().classLoader();
                        clz = classLoader.loadClass(info.path);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    if(clz == null){
                        throw new RuntimeException(" clz == null");
                    }
                    Intent intent = new Intent(context,clz);
                    context.startActivity(intent);
                }

            }

            //  更新当前插件
            lastPlugin = currentIPlugin;
            currentIPlugin = plugin;
        }
        return currentIPlugin;
    }

    @Override
    public IPlugin back() {
        return null;
    }

    @Override
    public int back0() {
        //  这个变量是用来记录前进时的  前一个当前路由      如果一旦后退，清空不记录
        lastPlugin = null;

        //check();

        if(size() <= 1){
            currentIPlugin = null;
            pluginStack.clear();
            pluginsLevel1NotInStack.clear();
            return ROUTER_BACK_EMPTY;
        }

        IPlugin tmpPlugin = null;
        IPlugin plugin0 = pluginStack.get(pluginStack.size() - 1);
        if(plugin0 != currentIPlugin){
            tmpPlugin = plugin0;
        }else{
            tmpPlugin = pluginStack.get(pluginStack.size() - 2);
        }

        RouterUtil.RouterBean rbC = RouterUtil.parse(currentIPlugin.key());
        RouterUtil.RouterBean rbL = RouterUtil.parse(tmpPlugin.key());

        if((rbC.module_name+rbC.router_level_1).equals(rbL.module_name+rbL.router_level_1)){
            //  fragment之间的切换
            if(rbL.level == 2){
                IPlugin pluginParent = (IPlugin) tmpPlugin.parent();
                FragmentActivity fa = (FragmentActivity) pluginParent.wrap();

                Fragment fraOrigin = (Fragment) currentIPlugin.wrap();
                Fragment fraNow = (Fragment) tmpPlugin.wrap();

                Log.d("wsdc1", "back back "+currentIPlugin.key()+"    ->    "+tmpPlugin.key());
                FragmentTransaction transaction = fa.getSupportFragmentManager().beginTransaction();
                transaction
                        .remove(fraOrigin)
                        .add(pluginParent.childLayout(),fraNow)
                        .show(fraNow)
                        .commit();

                pluginStack.remove(currentIPlugin);
                currentIPlugin = tmpPlugin;

                return ROUTER_BACK_FRAGMENT;
            }
        }

        pluginStack.remove(currentIPlugin);
        currentIPlugin = tmpPlugin;
        return ROUTER_BACK_ACTIVITY;
    }

    @Override
    public IPlugin home() {
        /*
         *  借助Activity的新建栈
         *  <li>    系统会引导activity的退出
         */
        return null;
    }

    @Override
    public void close(IPlugin plugin) {
        /*
         *   close函数的工作意义
         *   <li>    这里是路由，只需要管理好路由的事情就好了
         *           <li>    超出路由的事情就不需要负责了 (比如父插件和子插件之间的互动)
         *
         *
         */
        List<IPlugin> tmp = new LinkedList<>();
        for (IPlugin p0 : pluginStack) {
            if(p0 == plugin || p0 == plugin.parent()){
                tmp.add(p0);
            }
        }
        pluginStack.removeAll(tmp);
    }

    @Override
    public IPlugin getExistsPluginLevel1(String key) {
        for (IPlugin plugin : pluginStack) {
            if((plugin.status() & IPlugin.STATUS_LEVEL_MASK) == 1){
                if(plugin.key().equals(key)){
                    return plugin;
                }
            }else{
                IPlugin parent = (IPlugin) plugin.parent();
                if(parent.key().equals(key)){
                    return parent;
                }
            }
        }

        for (IPlugin parent : pluginsLevel1NotInStack) {
            if(parent.key().equals(key)){
                return parent;
            }
        }
        return null;
    }

    @Override
    public IData data() {
        return data;
    }

    @Override
    public IPlugin currentPlugin() {
        return currentIPlugin;
    }

    @Override
    public XInfoAll infoAll() {
        return infoAll;
    }

    @Override
    public int size() {
        return pluginStack.size();
    }

    @Override
    public void clear() {
        lastPlugin = null;
        currentIPlugin = null;
        pluginStack.clear();
        pluginsLevel1NotInStack.clear();
    }

    @Override
    public void check() {
        Log.d("wsdc1", "-------------------------------------------");
        Log.d("wsdc1", "router size = "+pluginStack.size());
        for (IPlugin plugin : pluginStack) {
            Log.d("wsdc1", "key = "+plugin.key());
        }
        Log.d("wsdc1", "not router size = "+pluginsLevel1NotInStack.size());
        Log.d("wsdc1", "status is null  "+(currentIPlugin == null)+"    "+(lastPlugin == null));
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        /*
         *  Activity的启动，联合当前路由所处的插件进行判断
         *  <li>    如果当前插件是二级插件，此时需要调用Fragment进行处理
         *  <li>    刚刚启动，是不会有任何Fragment在里面的
         */
        if(getLevel(currentIPlugin) == 2){
            final FragmentActivity fa = (FragmentActivity) activity;
            FragmentTransaction transaction = fa.getSupportFragmentManager().beginTransaction();

            Fragment fragment = (Fragment) currentIPlugin.wrap();
            IPlugin parent = (IPlugin) currentIPlugin.parent();
            transaction.add(parent.childLayout(),fragment);
            transaction.show(fragment);
            transaction.commit();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        /*
         *  如果需要后退到首页   (第一个路由)
         *  <li>    需要在这里清空路由信息 (可以能是fragment管理)
         *          <li>    清空fragment信息，只保留指定的一个
         */
    }

    @Override
    public void onActivityResumed(Activity activity) {
        /*
         *  是否需要引导Activity的退出
         *  <li>    等新的Activity显示的时候，才引导就得Activity退出
         *          <li>    不会出现黑屏
         *
         *  <li>    如果前一个插件的Activity不参与路由，那么引导Activity退出
         */
        if(lastPlugin != null){
            Activity act = null;
            IPlugin tmpPlugin = null;
            if(getLevel(lastPlugin) == 1){
                act = (Activity) lastPlugin.wrap();
                tmpPlugin = lastPlugin;
            }else{
                tmpPlugin = (IPlugin) lastPlugin.parent();
                act = (Activity) tmpPlugin.wrap();
            }

            if((tmpPlugin.status() & IPlugin.STATUS_START_SELF_MODE_MASK) == IPlugin.START_NOT_STACK){
                Log.d("wsdc", "上一个activity退出");
                act.finish();

                /*
                 *  如果插件在 没有入栈的插件表中，需要移除
                 */
                pluginsLevel1NotInStack.remove(tmpPlugin);
            }
        }

        //  清空lastPlugin    (内存问题)
        lastPlugin = null;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    private int getLevel(IPlugin plugin){
        int status = plugin.status();
        int rtn = status & IPlugin.STATUS_LEVEL_MASK;
        return rtn;
    }
}
