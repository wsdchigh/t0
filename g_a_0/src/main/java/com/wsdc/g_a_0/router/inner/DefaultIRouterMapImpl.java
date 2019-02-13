package com.wsdc.g_a_0.router.inner;

import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouterMap;

public class DefaultIRouterMapImpl implements IRouterMap {
    @Override
    public IPlugin get(String key,int mode) {
        /*
         *  需要检测plugin的合法性
         *  <li>    1   对应Activity
         *  <li>    2   对应Fragment
         */
        return null;
    }
}
