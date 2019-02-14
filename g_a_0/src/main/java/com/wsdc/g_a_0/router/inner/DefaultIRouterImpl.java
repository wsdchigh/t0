package com.wsdc.g_a_0.router.inner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

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
 */
public class DefaultIRouterImpl implements IRouter {
    IRouterMap routerMap ;
    IPlugin currentIPlugin;
    IData<Integer> data;

    List<IPlugin> pluginStack = new LinkedList<>();
    XInfoAll infoAll;

    public DefaultIRouterImpl(XInfoAll infoAll, Context context) {
        this.infoAll = infoAll;
        routerMap = new DefaultIRouterMapImpl(infoAll,context,this);
    }

    @Override
    public IRouterMap map() {
        return routerMap;
    }

    @Override
    public IPlugin go(String key, int mode) {
        final IPlugin plugin = routerMap.getRouterPlugin(key,mode);
        final IPlugin origin = currentIPlugin;
        if(plugin != null){
            switch (mode){
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
                    //  是一个Fragment parent=Activity
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
                 *
                 */
                if((plugin.status() & IPlugin.STATUS_LEVEL_MASK) == 1){
                    if((origin.status() & IPlugin.STATUS_LEVEL_MASK) == 1){
                        //
                        Activity activity = (Activity) origin.wrap();
                        Activity activityGo = (Activity) plugin.wrap();
                        Intent intent = new Intent(activity,activityGo.getClass());
                        activity.startActivity(intent);
                    }else{
                        IPlugin parent = (IPlugin) origin.wrap();
                        Activity activity = (Activity) parent.wrap();
                        Activity activityGo = (Activity) plugin.wrap();
                        Intent intent = new Intent(activity,activityGo.getClass());
                        activity.startActivity(intent);
                    }
                }else{
                    //
                    if((origin.status() & IPlugin.STATUS_LEVEL_MASK) == 1){
                        Activity originActivity = (Activity) origin.wrap();
                        IPlugin goParent = (IPlugin) plugin.parent();
                        Activity goActivity = (Activity) goParent.wrap();

                        Intent intent = new Intent(originActivity,goActivity.getClass());
                        originActivity.startActivity(intent);

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
                        }
                    }
                }
            }

            currentIPlugin = plugin;
        }
        return currentIPlugin;
    }

    @Override
    public IPlugin back() {
        return null;
    }

    @Override
    public IPlugin home() {
        return null;
    }

    @Override
    public IPlugin getExistsPluginLevel1(String key) {
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
}
