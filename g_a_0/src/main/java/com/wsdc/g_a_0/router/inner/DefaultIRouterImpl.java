package com.wsdc.g_a_0.router.inner;

import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouter;
import com.wsdc.g_a_0.router.IRouterMap;

/*
 *  默认路由实现
 *  ??? ???????
 */
public class DefaultIRouterImpl implements IRouter {
    @Override
    public IRouterMap map() {
        return null;
    }

    @Override
    public IPlugin go(String key, int type) {
        return null;
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
    public IData data() {
        return null;
    }

    @Override
    public IPlugin currentPlugin() {
        return null;
    }
}
