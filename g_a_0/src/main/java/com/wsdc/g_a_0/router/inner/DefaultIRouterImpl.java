package com.wsdc.g_a_0.router.inner;

import android.app.Activity;
import android.content.Context;
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


            /*
             *  程序启动调用go的时候，origin == null
             *  <li>    因为之前没有任何数据
             *  <li>    系统自动跳转到了 Main 参数的Activity
             *          <li>    已经使用了startIntent()
             *          <li>    不需要再次调用了
             */
            if(origin == null){
                if((plugin.status() & IPlugin.STATUS_LEVEL) == 1){
                    //  是一个Activity
                    //  可以没有任何处理
                }else{
                    //  是一个Fragment parent=Activity
                    IPlugin<Activity,Integer> parent = (IPlugin<Activity,Integer>) plugin.parent();
                    Activity activity = parent.wrap();
                    List<IPlugin> children = parent.children();

                    //final IPlugin<Fragment,Integer> second = plugin;
                    //Fragment fragment = second.wrap();

                    FragmentActivity activity0 = (FragmentActivity) activity;
                    //  开启activity事务
                    FragmentTransaction transaction = activity0.getSupportFragmentManager().beginTransaction();

                    //  如果里面只有一个 那么就是自己     因为前者不存在，所以不需要添加过渡特效
                    if(children.size() == 1){
                        IPlugin<Fragment,Integer> iPlugin = children.get(0);
                        Fragment wrap = iPlugin.wrap();
                        transaction.add(iPlugin.id(),wrap);
                        transaction.show(wrap);

                        transaction.commit();
                    }else{
                        /*
                         *  如果里面有多个，那么最后一个是自己，需要和前面那个做出一个过渡效果
                         *  <li>    调用事务移除前面那个，添加自己，展示自己
                         *  <li>    添加过渡特效
                         */
                        IPlugin<Fragment,Integer> iPlugin = children.get(children.size()-1);
                        IPlugin<Fragment,Integer> iPlugin1 = children.get(children.size()-2);
                        Fragment wrap = iPlugin.wrap();
                        transaction.add(iPlugin.id(),wrap);
                        transaction.remove(iPlugin1.wrap());
                        transaction.show(wrap);
                        //  添加过渡特效
                        transaction.commit();
                    }
                }
            }else{

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
