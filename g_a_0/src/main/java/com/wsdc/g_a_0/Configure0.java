package com.wsdc.g_a_0;

import java.util.List;

/*
 *  和配置文件完全对应
 *  <li>    配置文件可以解析出最多3份配置
 */
public class Configure0 {
    public List<Apk0> apks;
    public GuidePage gp;
    public WrapInfo infos;
    public RouteInfo routeInfo;

    public static class Apk0{
        //  名字携带了版本信息 原样存储即可
        public String name;

        //  下载的地址
        public String url;

        public List<Plugin0> plugins;
    }



    public static class Plugin0{
        public String key;
        public String path;
        public String proxyPath;
        public String viewHolderPath;
        public String iDataPath;
        public boolean userParent;
        public int wrapKey;
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
    }

    /*
     *  配置路由的基本信息
     */
    public static class RouteInfo{
        public String path;
    }
}
