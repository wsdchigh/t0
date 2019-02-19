package com.wsdc.plugin_test.plugin.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;
import com.wsdc.g_a_0.router.IRouter;
import com.wsdc.plugin_test.GK;
import com.wsdc.plugin_test.R;

@APlugin(key="/test/home",sign=PluginSign.VIEW_HOLDER)
public class HomeViewHolder extends AbstractIViewHolder<Activity> implements RadioGroup.OnCheckedChangeListener {
    RadioButton rbHome0,rbCategory,rbCart,rbUser;
    RadioGroup rg;
    IRouter router = Starter.getInstance().getRouter();
    RadioButton lastRB;

    public HomeViewHolder(IPlugin<Activity, Integer> plugin) {
        super(plugin);
    }

    @Override
    protected void clear() {

    }

    @Override
    public View install(Context context, Activity activity, ViewGroup parent) {
        this.context = context;
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

        lastRB = rbHome0;

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
                update0(rbHome0);
                proxy().proxy(GK.HOME_TO_HOME0);
                break;

            case R.id.test_home_rb_category:
                update0(rbCategory);
                proxy().proxy(GK.HOME_TO_CATEGORY);
                break;

            case R.id.test_home_rb_cart:
                update0(rbCart);
                proxy().proxy(GK.HOME_TO_CART);
                break;

            case R.id.test_home_rb_user:
                update0(rbUser);
                proxy().proxy(GK.HOME_TO_USER);
                break;

        }
    }

    private void update0(RadioButton now){
        if(lastRB != null){
            lastRB.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.drawable.test_home_home0_menu_not_checked),null,null);
            lastRB.setTextColor(0xffc9c9c9);
            lastRB.invalidate();
        }
        now.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.drawable.test_home_home0_menu_checked),null,null);
        now.setTextColor(0xffff0000);
        now.invalidate();
        lastRB = now;
    }
}
