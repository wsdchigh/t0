package com.wsdc.g_a_0.router.inner;

import android.content.Context;
import android.util.Log;

import com.wsdc.g_a_0.APK;
import com.wsdc.g_a_0.DefaultAPK;
import com.wsdc.g_a_0.RouterUtil;
import com.wsdc.g_a_0.XInfo;
import com.wsdc.g_a_0.XInfoAll;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.DefaultPlugin;
import com.wsdc.g_a_0.router.IRouter;
import com.wsdc.g_a_0.router.IRouterMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultIRouterMapImpl implements IRouterMap {
    XInfoAll infoAll;
    Map<String,APK> apkMap = new HashMap<>();

    //  持有一个主APP的context对象
    Context context;
    IRouter router;

    public DefaultIRouterMapImpl(XInfoAll infoAll, Context context,IRouter router) {
        this.infoAll = infoAll;
        this.router = router;

        //  预先加载所有的APK信息
        List<XInfo> infos = infoAll.infos;


        /*
         *  如果存在依赖模块
         *  <li>    如果存在模块，那么使用依赖模块作为其他模块的父 ClassLoader
         *          <li>    避免公共模块多次加载  (如果存在多个插件的话)
         */
        APK rely = null;
        for (XInfo info : infos) {
            if("rely".equals(info.module_name)){
                rely = new DefaultAPK(info,context,this.getClass().getClassLoader());
            }
        }
        ClassLoader parent = rely == null? getClass().getClassLoader() : rely.classLoader();
        for (XInfo info : infos) {
            if(!"rely".equals(info.module_name)){
                APK apk = new DefaultAPK(info,context,parent);
                apkMap.put(info.module_name,apk);
            }
        }
    }

    @Override
    public IPlugin getRouterPlugin(String key, int mode) {
        IPlugin rtn = null;
        /*
         *  需要检测plugin的合法性
         *  <li>    1   对应Activity
         *  <li>    2   对应Fragment
         *
         *  <li>    如果插件只有一级
         *          <li>    那么允许存在多个相同的插件
         *
         *  <li>    如果插件有两级
         *          <li>    那么二级插件的一级插件只存在一个
         *          <li>    二级插件可以重复，可以不同
         *          <li>    也就是说，如果是二级插件，那么需要在路由中查找是否存在对应的一级插件
         *                  <li>    因为对应的一级插件只能有一个
         */
        RouterUtil.RouterBean parse = RouterUtil.parse(key);
        APK apk = apkMap.get(parse.module_name);

        if(parse.level == 2){
            //  注意  格式的匹配
            IPlugin parent = router.getExistsPluginLevel1("/"+parse.module_name+parse.router_level_1);
            Log.d("wsdc", "parent path = "+"/"+parse.module_name+parse.router_level_1);
            if(parent == null){
                parent = new DefaultPlugin(router,"/"+parse.module_name+parse.router_level_1,apk,infoAll);

                //  将传入进来的父插件启动参数 设置为父插件自身的启动参数
                int status = parent.status();
                status = status | ((mode & IPlugin.STATUS_START_PARENT_MODE_MASK) << 2);
                parent.updateStatus(status);

                Log.d("wsdc", "没有找到父插件");
            }else{
                Log.d("wsdc", "找到父插件");
            }
            rtn = new DefaultPlugin(router,key,apk,parent,infoAll);
        }else{
            rtn = new DefaultPlugin(router,key,apk,infoAll);
        }

        //  给plugin添加启动模式   需要先清空 然后在添加
        int status = rtn.status();
        status = status & (~IPlugin.STATUS_START_SELF_MODE_MASK);
        status = status | mode;
        rtn.updateStatus(status);
        return rtn;
    }

    @Override
    public IPlugin getNormalPlugin(String key) {
        IPlugin rtn = null;
        RouterUtil.RouterBean parse = RouterUtil.parse(key);
        APK apk = apkMap.get(parse.module_name);
        rtn = new DefaultPlugin(null,key,apk,infoAll);
        return rtn;
    }

    @Override
    public Map<String, APK> apkMap() {
        return apkMap;
    }


    @Override
    public XInfoAll infoAll() {
        return infoAll;
    }
}
