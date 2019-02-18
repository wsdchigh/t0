package com.wsdchigh.plugin_test;

import com.wsdc.g_a_0.ConfigureCreator;
import com.wsdc.g_a_0.XInfo;
import com.wsdc.g_a_0.XInfoAll;
import com.wsdc.g_a_0.base.BaseFragment;
import com.wsdc.g_a_0.plugin.inner.DefaultMainIData;
import com.wsdc.plugin_test.MainActivity;
import com.wsdc.plugin_test.R;
import com.wsdc.plugin_test.plugin.global.GlobalProxy;
import com.wsdc.plugin_test.plugin.global.GlobalViewHolder;
import com.wsdc.plugin_test.plugin.guide.GuideProxy;
import com.wsdc.plugin_test.plugin.guide.GuideViewHolder;
import com.wsdc.plugin_test.plugin.guide.guide0.Guide0Proxy;
import com.wsdc.plugin_test.plugin.guide.guide0.Guide0ViewHolder;
import com.wsdc.plugin_test.plugin.guide.welcome.WelcomeProxy;
import com.wsdc.plugin_test.plugin.guide.welcome.WelcomeViewHolder;
import com.wsdc.plugin_test.plugin.home.HomeProxy;
import com.wsdc.plugin_test.plugin.home.HomeViewHolder;
import com.wsdc.plugin_test.plugin.home.cart.CartProxy;
import com.wsdc.plugin_test.plugin.home.cart.CartViewHolder;
import com.wsdc.plugin_test.plugin.home.category.CategoryProxy;
import com.wsdc.plugin_test.plugin.home.category.CategoryViewHolder;
import com.wsdc.plugin_test.plugin.home.home0.Home0Proxy;
import com.wsdc.plugin_test.plugin.home.home0.Home0ViewHolder;
import com.wsdc.plugin_test.plugin.home.user.UserProxy;
import com.wsdc.plugin_test.plugin.home.user.UserViewHolder;
import com.wsdc.plugin_test.wrap.acitivity.HomeActivity;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class Creator {
    @Test
    public void create(){
        ConfigureCreator cc = new ConfigureCreator();

        if(false){
            cc.clear();
            return;
        }

        //  生成当前的wrap信息
        List<XInfo.WrapInfo> wrapInfos = new LinkedList<>();
        XInfo.WrapInfo info0 = new XInfo.WrapInfo();
        info0.path = MainActivity.class.getName();
        info0.wrapKey = 101;
        info0.type = "activity";
        wrapInfos.add(info0);

        info0 = new XInfo.WrapInfo();
        info0.path = HomeActivity.class.getName();
        info0.wrapKey = 102;
        info0.type = "activity";
        wrapInfos.add(info0);

        info0 = new XInfo.WrapInfo();
        info0.path = BaseFragment.class.getName();
        info0.wrapKey = 200;
        info0.type = "fragment";
        wrapInfos.add(info0);

        cc.wrapJson(wrapInfos);

        //  生成APK信息
        XInfo info = new XInfo();
        info.module_name = "test";
        info.name = "test-4.4.4.apk";
        info.version = "4.4.4";
        info.local_url = info.name;
        info.http_url = "";
        info.local = true;
        info.plugins = new LinkedList<>();

        //  引导界面的   Activity
        XInfo.XPlugin plugin0 = new XInfo.XPlugin();
        info.plugins.add(plugin0);
        plugin0.key = "/test/guide";
        plugin0.fragmentContainerID=R.id.test_guide_container;
        plugin0.wrapKey = 101;
        plugin0.userParent = false;
        plugin0.proxyPath = GuideProxy.class.getName();
        plugin0.viewHolderPath = GuideViewHolder.class.getName();
        plugin0.iDataPath = DefaultMainIData.class.getName();

        //  引导界面的   Fragment    (第一个显示的界面)
        plugin0 = new XInfo.XPlugin();
        info.plugins.add(plugin0);
        plugin0.key = "/test/guide/guide0";
        plugin0.wrapKey = 200;
        plugin0.userParent = false;
        plugin0.proxyPath = Guide0Proxy.class.getName();
        plugin0.viewHolderPath = Guide0ViewHolder.class.getName();
        plugin0.iDataPath = DefaultMainIData.class.getName();

        //  引导界面的第二个fragment    如果第一次使用，跳转到这个页面
        plugin0 = new XInfo.XPlugin();
        info.plugins.add(plugin0);
        plugin0.key = "/test/guide/welcome";
        plugin0.wrapKey = 200;
        plugin0.userParent = false;
        plugin0.proxyPath = WelcomeProxy.class.getName();
        plugin0.viewHolderPath = WelcomeViewHolder.class.getName();
        plugin0.iDataPath = DefaultMainIData.class.getName();

        //  全局插件
        XInfo.XPlugin globalPlugin = new XInfo.XPlugin();
        info.plugins.add(globalPlugin);
        globalPlugin.key = "/test/global_main";
        globalPlugin.wrapKey = 400;
        globalPlugin.userParent = false;
        globalPlugin.proxyPath = GlobalProxy.class.getName();
        globalPlugin.viewHolderPath = GlobalViewHolder.class.getName();
        globalPlugin.iDataPath = DefaultMainIData.class.getName();

        //  首页 插件   展示首屏数据          (Activity)
        plugin0 = new XInfo.XPlugin();
        info.plugins.add(plugin0);
        plugin0.key = "/test/home";
        plugin0.fragmentContainerID=R.id.test_home_act_container;
        plugin0.wrapKey = 102;
        plugin0.userParent = false;
        plugin0.proxyPath = HomeProxy.class.getName();
        plugin0.viewHolderPath = HomeViewHolder.class.getName();
        plugin0.iDataPath = DefaultMainIData.class.getName();

        //  首页中的第一屏 Fragment
        plugin0 = new XInfo.XPlugin();
        info.plugins.add(plugin0);
        plugin0.key = "/test/home/home0";
        plugin0.wrapKey = 200;
        plugin0.userParent = false;
        plugin0.proxyPath = Home0Proxy.class.getName();
        plugin0.viewHolderPath = Home0ViewHolder.class.getName();
        plugin0.iDataPath = DefaultMainIData.class.getName();

        //  首页中的第二屏     类目
        plugin0 = new XInfo.XPlugin();
        info.plugins.add(plugin0);
        plugin0.key = "/test/home/category";
        plugin0.wrapKey = 200;
        plugin0.userParent = false;
        plugin0.proxyPath = CategoryProxy.class.getName();
        plugin0.viewHolderPath = CategoryViewHolder.class.getName();
        plugin0.iDataPath = DefaultMainIData.class.getName();

        //  首页第三屏       购物车
        plugin0 = new XInfo.XPlugin();
        info.plugins.add(plugin0);
        plugin0.key = "/test/home/cart";
        plugin0.wrapKey = 200;
        plugin0.userParent = false;
        plugin0.proxyPath = CartProxy.class.getName();
        plugin0.viewHolderPath = CartViewHolder.class.getName();
        plugin0.iDataPath = DefaultMainIData.class.getName();

        //  首页第四屏       用户界面
        plugin0 = new XInfo.XPlugin();
        info.plugins.add(plugin0);
        plugin0.key = "/test/home/user";
        plugin0.wrapKey = 200;
        plugin0.userParent = false;
        plugin0.proxyPath = UserProxy.class.getName();
        plugin0.viewHolderPath = UserViewHolder.class.getName();
        plugin0.iDataPath = DefaultMainIData.class.getName();

        cc.infoJson(info);

        XInfoAll infoAll = new XInfoAll();
        infoAll.version = "4.4.4";
        infoAll.versionCode = 444;
        //  创建apk.json
        cc.infoAllJson(infoAll);
    }
}
