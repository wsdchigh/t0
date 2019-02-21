package com.wsdc.plugin_test.plugin.home.home0;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;
import com.wsdc.plugin_test.R;
import com.wsdc.plugin_test.adapters.Home0ADAdapter;
import com.wsdc.plugin_test.entities_static.GoodsBean;
import com.wsdc.plugin_test.view_wrap.CommonVPWrap;

import java.util.LinkedList;
import java.util.List;

@APlugin(key="/test/home/home0",sign=PluginSign.VIEW_HOLDER)
public class Home0ViewHolder extends AbstractIViewHolder<Fragment> {
    ViewFlipper vf;
    ViewPager vp;
    Home0ADAdapter adAdapter;
    List<GoodsBean> data = new LinkedList<>();
    CommonVPWrap vpWrap;

    {
        GoodsBean bean = new GoodsBean();
        data.add(bean);

        bean = new GoodsBean();
        data.add(bean);

        bean = new GoodsBean();
        data.add(bean);
    }

    public Home0ViewHolder(IPlugin<Fragment, Integer> plugin) {
        super(plugin);
    }

    @Override
    protected void clear() {
        if(vpWrap != null){
            vpWrap.close();
        }
    }

    @Override
    public View install(Context context, Fragment fragment, ViewGroup parent) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.home_home0,parent,false);
        return rootView;
    }

    @Override
    public void notify0(int type, Integer key, IData iData) {

    }

    @Override
    public void init(Context context) {
        vp = rootView.findViewById(R.id.test_home_home0_vp);
        vf = rootView.findViewById(R.id.test_home_home0_vf);

        adAdapter = new Home0ADAdapter(context);
        vp.setAdapter(adAdapter);
        adAdapter.setData(data);

        vpWrap = new CommonVPWrap(vp,plugin.handler());

    }

    @Override
    public void exit(Context context) {

    }
}
