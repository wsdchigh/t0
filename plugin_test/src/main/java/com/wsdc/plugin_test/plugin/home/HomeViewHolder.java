package com.wsdc.plugin_test.plugin.home;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;
import com.wsdc.plugin_test.R;

@APlugin(key="/test/home",sign=PluginSign.VIEW_HOLDER)
public class HomeViewHolder extends AbstractIViewHolder<Activity> implements RadioGroup.OnCheckedChangeListener {
    RadioButton rbHome0,rbCategory,rbCart,rbUser;
    RadioGroup rg;

    public HomeViewHolder(IPlugin<Activity, Integer> plugin) {
        super(plugin);
    }

    @Override
    protected void clear() {

    }

    @Override
    public View install(Context context, Activity activity, ViewGroup parent) {
        rootView = LayoutInflater.from(context).inflate(R.layout.home_act,parent,false);
        return rootView;
    }

    @Override
    public void notify0(int type, Integer key, IData iData) {

    }

    @Override
    public void init(Context context) {
        rbHome0 = rootView.findViewById(R.id.test_home_rb_home0);
        rbCategory = rootView.findViewById(R.id.test_home_rb_category);
        rbCart = rootView.findViewById(R.id.test_home_rb_cart);
        rbUser = rootView.findViewById(R.id.test_home_rb_user);

        rg = (RadioGroup) rbHome0.getParent();
        rg.setOnCheckedChangeListener(this);
    }

    @Override
    public void exit(Context context) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.test_home_rb_home0:
                Log.d("wsdc", "首页");
                break;

            case R.id.test_home_rb_category:
                Log.d("wsdc", "分类");
                break;

            case R.id.test_home_rb_cart:
                Log.d("wsdc", "购物车");
                break;

            case R.id.test_home_rb_user:
                Log.d("wsdc", "用户");
                break;

        }
    }
}
