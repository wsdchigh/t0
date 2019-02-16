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

import com.wsdc.g_a_0.XInfoAll;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouter;
import com.wsdc.g_a_0.router.IRouterMap;

import java.util.LinkedList;
import java.util.List;

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
    IData<Integer> data;

    List<IPlugin> pluginStack = new LinkedList<>();
    XInfoAll infoAll;

    public DefaultIRouterImpl(XInfoAll infoAll, Context context) {
        this.infoAll = infoAll;
        routerMap = new DefaultIRouterMapImpl(infoAll,context,this);

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
        final IPlugin plugin = routerMap.getRouterPlugin(key,mode);
        final IPlugin origin = currentIPlugin;
        if(plugin != null){
            switch (mode & IPlugin.STATUS_START_SELF_MODE_MASK){
                case IPlugin.START_COMMON:
                    //  添加到栈里面
                    pluginStack.add(plugin);
                    break;

                case IPlugin.START_NOT_STACK:
                    //  不添加到栈里面
                    break;

                default:
                    throw new RuntimeException("不支持的启动模式");
            }
            /*
             *  只有一种情况会触发   (origin)
             *  <li>    APP启动的时候，会等于null
             *          <li>    其余情况一律不会
             */
            if(origin == null){
                //  因为前面没有，所以不需要对前面进行处理
                if((plugin.status() & IPlugin.STATUS_LEVEL_MASK) == 1){
                    //  是一个Activity
                    //  可以没有任何处理
                }else{
                    /*
                     *  <li>    Activity暂时没有初始化
                     *  <li>    Activity暂时没启动
                     */
                    IPlugin<Activity,Integer> parent = (IPlugin<Activity,Integer>) plugin.parent();
                    Activity activity = parent.wrap();
                    List<IPlugin> children = parent.children();

                    FragmentActivity activity0 = (FragmentActivity) activity;
                    //  开启activity事务
                    FragmentTransaction transaction = activity0.getSupportFragmentManager().beginTransaction();

                    //  只有自己
                    IPlugin<Fragment,Integer> iPlugin = children.get(0);
                    Fragment wrap = iPlugin.wrap();
                    transaction.add(iPlugin.id(),wrap);
                    transaction.show(wrap);
                    transaction.commit();
                }

            }else{
                /*
                 *  <li>    如果涉及到的是两个Activity之间的切换
                 *          <li>    让两个Activity执行过渡特效
                 *
                 *  <li>    如果是涉及一个Activity的fragment切换
                 *          <li>    让fragment执行切换特效
                 *
                 *  <li>    之所以携带一个父插件的启动参数
                 *          <li>    如果涉及到两个activity的跳转
                 *          <li>    如果此时是二级路由，那么需要判断前一个activity是否需要关闭   (根据父插件启动参数决定)
                 *                  <li>    父插件启动参数，只有第一个设置的有效，之后的设置均无效
                 */
                if((plugin.status() & IPlugin.STATUS_LEVEL_MASK) == 1){
                    if((origin.status() & IPlugin.STATUS_LEVEL_MASK) == 1){


                        Activity activity = (Activity) origin.wrap();
                        Activity activityGo = (Activity) plugin.wrap();
                        Intent intent = new Intent(activity,activityGo.getClass());
                        activity.startActivity(intent);

                        //  是否需要关闭activity
                        if((origin.status() & IPlugin.STATUS_START_SELF_MODE_MASK) == IPlugin.START_NOT_STACK){
                            //  插件没有入栈 只需要引导退出即可
                            activity.finish();
                        }
                    }else{
                        IPlugin parent = (IPlugin) origin.wrap();
                        Activity activity = (Activity) parent.wrap();
                        Activity activityGo = (Activity) plugin.wrap();
                        Intent intent = new Intent(activity,activityGo.getClass());
                        activity.startActivity(intent);

                        //  是否需要关闭activity
                        if((origin.status() & IPlugin.STATUS_START_SELF_MODE_MASK) == IPlugin.START_NOT_STACK){
                            /*
                             *  一级插件不入栈，但是下面的二级插件可能入栈  所以需要清除对应插件信息
                             *  <li>    引导activity的退出
                             *  <li>    插件在卸载的时候自动清除栈中信息
                             */
                            activity.finish();
                        }
                    }
                }else{
                    //
                    if((origin.status() & IPlugin.STATUS_LEVEL_MASK) == 1){
                        Activity originActivity = (Activity) origin.wrap();
                        IPlugin goParent = (IPlugin) plugin.parent();
                        Activity goActivity = (Activity) goParent.wrap();

                        Intent intent = new Intent(originActivity,goActivity.getClass());
                        originActivity.startActivity(intent);

                        //  是否需要关闭activity
                        if((origin.status() & IPlugin.STATUS_START_SELF_MODE_MASK) == IPlugin.START_NOT_STACK){
                            //  插件没有入栈 只需要引导退出即可
                            originActivity.finish();
                        }

                    }else{
                        IPlugin originParent = (IPlugin) origin.parent();
                        Activity originActivity = (Activity) originParent.wrap();
                        IPlugin goParent = (IPlugin) plugin.parent();
                        Activity goActivity = (Activity) goParent.wrap();

                        if(originActivity == goActivity){
                            //  使用fragment执行过渡特效
                            final FragmentActivity act = (FragmentActivity) originActivity;
                            FragmentTransaction transaction = act.getSupportFragmentManager().beginTransaction();

                            Fragment originFragment = (Fragment) origin.wrap();
                            Fragment nowFragment = (Fragment) plugin.wrap();

                            transaction.add(goParent.id(),nowFragment);
                            transaction.remove(originFragment);
                            transaction.show(nowFragment);

                            transaction.commit();
                        }else{
                            //  使用activity执行过渡特效
                            Intent intent = new Intent(originActivity,goActivity.getClass());
                            originActivity.startActivity(intent);

                            //  是否需要关闭activity
                            if((origin.status() & IPlugin.STATUS_START_SELF_MODE_MASK) == IPlugin.START_NOT_STACK){
                                //  插件没有入栈 只需要引导退出即可
                                originActivity.finish();
                            }
                        }
                    }
                }
            }

            //  更新当前插件
            currentIPlugin = plugin;
            //  插件是否需要入栈
            if((plugin.status() & IPlugin.STATUS_START_SELF_MODE_MASK) == IPlugin.START_COMMON){
                pluginStack.add(plugin);
            }
        }
        return currentIPlugin;
    }

    @Override
    public IPlugin back() {
        /*
         *  路由后退一步
         */
        return null;
    }

    @Override
    public IPlugin home() {
        /*
         *  只留下栈中的第一个插件，其他的均清空
         *  <li>    收集所有的activity   集体引导finish
         *          <li>    除了第一个activity和最后一个activity，集体引导销毁   (finish)
         *          <li>    调用一次back，此时就能够正常的抵达首个Activity之中
         *
         *          <li>    此时，插件的tag打上标记
         *                  <li>    pause函数时候，验证tag标记，执行Fragment的事务操作
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

    }

    @Override
    public void onActivityResumed(Activity activity) {

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
