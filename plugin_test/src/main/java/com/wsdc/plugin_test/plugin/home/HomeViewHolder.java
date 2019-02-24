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
import com.wsdc.plugin_test.R;
import com.wsdc.plugin_test.GK;

@APlugin(key="/test/home",sign=PluginSign.VIEW_HOLDER)
public class HomeViewHolder extends AbstractIViewHolder<Activity> implements RadioGroup.OnCheckedChangeListener {
    RadioButton rbHome0,rbCategory,rbCart,rbUser;
    RadioGroup rg;
    IRouter router = Starter.getInstance().getRouter();
    RadioButton lastRB;

    //  rg的切换是否需要操作路由
    //  主动点击需要带动路由，路由的后退驱动rg，此时不需要再次操作路由
    boolean shouldRouter = true;

    public HomeViewHolder(IPlugin<Activity, Integer> plugin) {
        super(plugin);
    }

    @Override
    public View install(Context context, Activity activity, ViewGroup parent) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.home_act,parent,false);
        return rootView;
    }

    @Override
    public void notify0(int type, Integer key, IData iData) {
        switch (type){
            case IData.TYPE_INIT:

                break;

            case IData.TYPE_SINGLE:
                switch (key){
                    /*
                     *  后退需要响应底层的RadioGroup的切换
                     *  <li>    此时标记为不需要驱动路由
                     *  <li>    设置具体的rb为选中  驱动监听做出选择
                     */
                    case GK.HOME_BACK_SWITCH:
                        String s = (String) plugin().data().get(key);
                        shouldRouter = false;
                        switch (s){
                            case GK.ROUTE_HOME_HOME0:
                                rbHome0.setChecked(true);
                                break;

                            case GK.ROUTE_HOME_CATEGORY:
                                rbCategory.setChecked(true);
                                break;

                            case GK.ROUTE_HOME_CART:
                                rbCart.setChecked(true);
                                break;

                            case GK.ROUTE_HOME_USER:
                                rbUser.setChecked(true);
                                break;
                        }
                        break;
                }
                break;
        }
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
                if(shouldRouter){
                    proxy().proxy(GK.HOME_TO_HOME0);
                }else{
                    shouldRouter = !shouldRouter;
                }

                break;

            case R.id.test_home_rb_category:
                update0(rbCategory);
                if(shouldRouter){
                    proxy().proxy(GK.HOME_TO_CATEGORY);
                }else{
                    shouldRouter = !shouldRouter;
                }
                break;

            case R.id.test_home_rb_cart:
                update0(rbCart);
                if(shouldRouter){
                    proxy().proxy(GK.HOME_TO_CART);
                }else{
                    shouldRouter = !shouldRouter;
                }

                break;

            case R.id.test_home_rb_user:
                update0(rbUser);
                if(shouldRouter){
                    proxy().proxy(GK.HOME_TO_USER);
                }else{
                    shouldRouter = !shouldRouter;
                }

                break;

        }
    }

    private void update0(RadioButton now){
        if(lastRB != null){

            lastRB.setTextColor(0xff000000);
            if(lastRB ==rbHome0){
                lastRB.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.mipmap.icon_home_home0),null,null);
            }else if(lastRB == rbCategory){
                lastRB.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.mipmap.icon_home_category),null,null);
            }else if(lastRB == rbCart){
                lastRB.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.mipmap.icon_home_cart),null,null);
            }else if(lastRB == rbUser){
                lastRB.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.mipmap.icon_home_person),null,null);
            }
            lastRB.invalidate();
        }

        now.setTextColor(0xffff0000);
        if(now ==rbHome0){
            now.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.mipmap.icon_home_home0_checked),null,null);
        }else if(now == rbCategory){
            now.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.mipmap.icon_home_category_checked),null,null);
        }else if(now == rbCart){
            now.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.mipmap.icon_home_cart_checked),null,null);
        }else if(now == rbUser){
            now.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.mipmap.icon_home_person_checked),null,null);
        }
        now.invalidate();
        lastRB = now;
    }
}
