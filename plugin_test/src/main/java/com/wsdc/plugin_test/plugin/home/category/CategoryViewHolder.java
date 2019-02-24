package com.wsdc.plugin_test.plugin.home.category;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;
import com.wsdc.plugin_test.R;

@APlugin(key="/test/home/category",sign=PluginSign.VIEW_HOLDER)
public class CategoryViewHolder extends AbstractIViewHolder<Fragment> {

    public CategoryViewHolder(IPlugin<Fragment, Integer> plugin) {
        super(plugin);
    }

    @Override
    public View install(Context context, Fragment fragment, ViewGroup parent) {
        rootView = LayoutInflater.from(context).inflate(R.layout.home_category,parent,false);
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
