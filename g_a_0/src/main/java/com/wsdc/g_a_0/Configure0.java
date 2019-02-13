package com.wsdc.g_a_0;

import java.util.List;

/*
 *  和配置文件完全对应
 *  <li>    配置文件可以解析出最多3份配置
 */
public class Configure0 {
    public List<Apk0> apks;
    public GuidePage gp;
    public List<WrapInfo> wrapInfos;
    public RouteInfo routeInfo;

    public static class Apk0{
        //  名字携带了版本信息 原样存储即可
        public String name;

        //  下载的地址
        public String http_url;

        //  本地存放的地址
        public String local_url;

        //  存放的一系列插件信息
        public List<Plugin0> plugins;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public List<Plugin0> getPlugins() {
            return plugins;
        }

        public void setPlugins(List<Plugin0> plugins) {
            this.plugins = plugins;
        }
    }



    public static class Plugin0{
        public String key;
        public String path;
        public String proxyPath;
        public String viewHolderPath;
        public String iDataPath;
        public boolean userParent;
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
    }

    /*
     *  app启动一定是既然进入引导界面的
     */
    public static class GuidePage{

    }

    //  wrap配置信息
    public static class WrapInfo{
        public int wrapKey;
        public String path;
        public String wrapType;

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

        public String getWrapType() {
            return wrapType;
        }

        public void setWrapType(String wrapType) {
            this.wrapType = wrapType;
        }
    }

    /*
     *  配置路由的基本信息
     */
    public static class RouteInfo{
        public String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
