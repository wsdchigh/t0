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
    public static final int GUIDE0_TO_WELCOME = ++base;
    public static final int GUIDE0_TO_HOME = ++base;
    public static final int WELCOME_TO_HOME = ++base;

    //  首页模块
    public static final int HOME_TO_HOME0 = ++base;
    public static final int HOME_TO_CATEGORY = ++base;
    public static final int HOME_TO_CART = ++base;
    public static final int HOME_TO_USER = ++base;


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
