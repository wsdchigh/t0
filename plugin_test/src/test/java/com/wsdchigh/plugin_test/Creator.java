package com.wsdchigh.plugin_test;

import com.wsdc.g_a_0.ConfigureCreator;
import com.wsdc.g_a_0.XInfo;
import com.wsdc.g_a_0.XInfoAll;
import com.wsdc.g_a_0.plugin.inner.DefaultMainIData;
import com.wsdchigh.plugin_test.plugin.GuideProxy;
import com.wsdchigh.plugin_test.plugin.GuideViewHolder;
import com.wsdchigh.plugin_test.plugin.global.GlobalProxy;
import com.wsdchigh.plugin_test.plugin.global.GlobalViewHolder;

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

        cc.wrapJson(wrapInfos);

        //  生成APK信息
        XInfo info = new XInfo();
        info.module_name = "test";
        info.name = "test-4.4.4.apk";
        info.version = "4.4.4";
        info.local_url = "";
        info.http_url = "";
        info.local = true;
        info.plugins = new LinkedList<>();

        XInfo.XPlugin plugin0 = new XInfo.XPlugin();
        info.plugins.add(plugin0);
        plugin0.key = "/test/t0";
        plugin0.wrapKey = 101;
        plugin0.userParent = false;
        plugin0.proxyPath = GuideProxy.class.getName();
        plugin0.viewHolderPath = GuideViewHolder.class.getName();
        plugin0.iDataPath = DefaultMainIData.class.getName();

        XInfo.XPlugin globalPlugin = new XInfo.XPlugin();
        info.plugins.add(globalPlugin);
        globalPlugin.key = "/global/global_main";
        globalPlugin.wrapKey = 102;
        globalPlugin.userParent = false;
        globalPlugin.proxyPath = GlobalProxy.class.getName();
        globalPlugin.viewHolderPath = GlobalViewHolder.class.getName();
        globalPlugin.iDataPath = DefaultMainIData.class.getName();

        cc.infoJson(info);

        XInfoAll infoAll = new XInfoAll();
        infoAll.version = "4.4.4";
        infoAll.versionCode = 444;
        //  创建apk.json
        cc.infoAllJson(infoAll);
    }
}
