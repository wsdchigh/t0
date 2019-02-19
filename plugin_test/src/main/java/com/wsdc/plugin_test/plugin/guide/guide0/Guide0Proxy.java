package com.wsdc.plugin_test.plugin.guide.guide0;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;
import com.wsdc.plugin_test.GK;

import java.util.LinkedList;
import java.util.List;

public class Guide0Proxy extends AbstractIProxy<Fragment> {
    List<Integer> promiseKey = new LinkedList<>();

    {
        promiseKey.add(GK.GUIDE0_TO_WELCOME);
    }

    public Guide0Proxy(IPlugin<Fragment, Integer> plugin, Context context) {
        super(plugin, context);
    }

    @Override
    public boolean proxy0(Integer key, Object... args) {
        //  fragment activity 均不需要添加到路由之中
        switch (key){
            case GK.GUIDE0_TO_WELCOME:
                Log.d("wsdc1", "is null :"+(plugin() == null)+"     "+(plugin().router() == null));
                Log.d("wsdc1", ""+plugin().router().getClass().getName());
                plugin().router().go(GK.ROUTE_GUIDE_WELCOME,IPlugin.START_NOT_STACK | (IPlugin.START_NOT_STACK >> 2));
                Log.d("wsdc1", "点击去欢迎界面");
                break;
        }
        return false;
    }

    @Override
    protected List<Integer> keys() {
        return promiseKey;
    }
}
