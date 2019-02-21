package com.wsdc.plugin_test;

/*
 *  global key
 *  <li>    全局常量定义
 *  <li>    如果key的值没有任何含义，尽量使用Integer充当key而不是String
 */
public class GK {
    //  基石  以此自增        不同的模块，使用基数不一样即可
    private static int base = 1000;

    //  引导模块
    public static final int GUIDE0_TO_WELCOME = 401;
    public static final int GUIDE0_TO_HOME = 402;
    public static final int WELCOME_TO_HOME = 400;

    //  首页模块
    public static final int HOME_TO_HOME0 =403;
    public static final int HOME_TO_CATEGORY = 404;
    public static final int HOME_TO_CART = 405;
    public static final int HOME_TO_USER = 406;


    //  关于fragment的后退切换，需要发送下来
    public static final int BACK_PRESS = 500;

    //  home页面，后退切换的vh标记
    public static final int HOME_BACK_SWITCH = 407;



    //  路由信息定义表
    public static final String ROUTE_GUIDE_GUIDE0 = "/test/guide/guide0";
    public static final String ROUTE_GUIDE_WELCOME = "/test/guide/welcome";

    //  首页 首屏
    public static final String ROUTE_HOME_HOME0 = "/test/home/home0";

    //  首页 分类屏
    public static final String ROUTE_HOME_CATEGORY = "/test/home/category";

    //  首页 购物车屏
    public static final String ROUTE_HOME_CART = "/test/home/cart";

    //  首页 用户屏
    public static final String ROUTE_HOME_USER = "/test/home/user";
}
