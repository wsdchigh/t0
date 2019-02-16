package com.wsdc.g_a_0;

import java.util.List;

/*
 *  针对任何一个apk提供的信息
 *  <li>    如果主APP携带了插件信息
 *          <li>    独立编写插件的时候，所有的插件均在自己的app中
 *          <li>    编写期间需要独立测试插件    (任何插件均可以独立使用)
 *          <li>    需要内置一个描述自身APK的消息
 *          <li>    使用isLocal标记 当前信息描述的是主APK文件
 *                  <li>    module_name 正常填写
 *
 *          <li>    线上的APK是不携带任何插件的，之所以这么设定
 *                  <li>    让插件能够独立测试
 *                  <li>    插件测试，本身就属于主APK
 *
 *  <li>    主APK只会携带一些必要的数据
 *
 */
public class XInfo {
    //  模块的名称 唯一，相同的模块只能存在一个
    //  用户模块?登录模块?
    //  需要查询出 rely 这个模块 这个模块属于公共模块  classloader 代理其他模块
    public String module_name;

    //  apk的名字  用这个名字存储
    public String name;

    /*
     *  版本信息
     *  <li>    例如3.1.4
     *  <li>    如果主版本需要更新  那么更新apk
     *          <li>    否则，只分发插件
     *  <li>    比对，如果不相同，则下载更新
     */
    public String version;

    /*
     *  网络下载的url
     *  <li>    如果需要下载，调用这个url地址下载数据
     */
    public String http_url;

    /*
     *  本地存放的路径
     *  <li>    默认存放在files/start_wsdc/目录下面   (不提供修改)
     *  <li>    存储 的值为 name(同值)
     */
    public String local_url;

    public List<XPlugin> plugins;

    //  标记当前APK为自身APK，而不是其他的插件
    public boolean local;

    public static class XPlugin{
        //  路由的key 唯一
        public String key;
        //  使用系统提供的默认实现  不需要反射获取
        public String path;
        public String proxyPath;
        public String viewHolderPath;
        public String iDataPath;
        public boolean userParent;

        //  如果下面有Fragment，那么Activity需要提供一个位置(View)
        public int fragmentContainerID;

        //  根据key匹配一个或者多个Activity?Fragment
        /*
         *  wrap    规则
         *  100+    Activity
         *  200+    Fragment
         *  300+    其他(不参与路由)
         */
        public int wrapKey;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getProxyPath() {
            return proxyPath;
        }

        public void setProxyPath(String proxyPath) {
            this.proxyPath = proxyPath;
        }

        public String getViewHolderPath() {
            return viewHolderPath;
        }

        public void setViewHolderPath(String viewHolderPath) {
            this.viewHolderPath = viewHolderPath;
        }

        public String getiDataPath() {
            return iDataPath;
        }

        public void setiDataPath(String iDataPath) {
            this.iDataPath = iDataPath;
        }

        public boolean isUserParent() {
            return userParent;
        }

        public void setUserParent(boolean userParent) {
            this.userParent = userParent;
        }

        public int getWrapKey() {
            return wrapKey;
        }

        public void setWrapKey(int wrapKey) {
            this.wrapKey = wrapKey;
        }

        public int getFragmentContainerID() {
            return fragmentContainerID;
        }

        public void setFragmentContainerID(int fragmentContainerID) {
            this.fragmentContainerID = fragmentContainerID;
        }
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHttp_url() {
        return http_url;
    }

    public void setHttp_url(String http_url) {
        this.http_url = http_url;
    }

    public String getLocal_url() {
        return local_url;
    }

    public void setLocal_url(String local_url) {
        this.local_url = local_url;
    }

    public List<XPlugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<XPlugin> plugins) {
        this.plugins = plugins;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    /*
     *  记录服务的对象
     *  <li>    Activity
     *  <li>    Fragment
     *  <li>    View
     *  <li>    Dialog
     *  <li>    PopupWindow
     *  <li>    Service
     *  <li>    Broadcast
     */
    public static class WrapInfo{
        public int wrapKey;
        public String path;
        public String type;

        public int getWrapKey() {
            return wrapKey;
        }

        public void setWrapKey(int wrapKey) {
            this.wrapKey = wrapKey;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


}
