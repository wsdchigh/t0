package com.wsdc.plugin_test.plugin.guide;

import android.app.Activity;
import android.content.Context;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIProxy;
import com.wsdc.plugin_test.GK;
import com.wsdc.plugin_test.R;

import java.util.LinkedList;
import java.util.List;

@APlugin(key="/test/guide",sign=PluginSign.PROXY,wrapKey = 100,fragmentContainerID = R.id.test_guide_container)
public class GuideProxy extends AbstractIProxy<Activity> {
    private List<Integer> promiseKey = new LinkedList<>();

    {
        promiseKey.add(GK.BACK_PRESS);
    }

    public GuideProxy(IPlugin<Activity, Integer> plugin, Context context) {
        super(plugin, context);
    }

    @Override
    public boolean proxy0(Integer key, Object... args) {
        switch (key){
            case GK.BACK_PRESS:

                break;
        }
        return false;
    }

    @Override
    protected List<Integer> keys() {
        return promiseKey;
    }
}
