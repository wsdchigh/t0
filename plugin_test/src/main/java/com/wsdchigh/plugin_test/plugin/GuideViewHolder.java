package com.wsdchigh.plugin_test.plugin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;
import com.wsdchigh.plugin_test.R;

public class GuideViewHolder extends AbstractIViewHolder<Activity> {
    public GuideViewHolder(IPlugin<Activity, Integer> plugin, Context context) {
        super(plugin, context);
    }

    @Override
    public View install(Context context, Activity activity) {
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        return rootView;
    }

    @Override
    public void notify0(int type, Integer key, IData iData) {

    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void exit(Context context) {

    }
}
