package com.wsdc.plugin_test.plugin.guide.welcome;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;
import com.wsdc.plugin_test.R;
import com.wsdc.plugin_test.GK;
import com.wsdchigh.g_a_rely.adapters.CommonVPAdapter;
import com.wsdchigh.g_a_rely.views.XViewPager;

import java.util.LinkedList;
import java.util.List;

public class WelcomeViewHolder extends AbstractIViewHolder<Fragment>{
    XViewPager vp;
    CommonVPAdapter<Integer> adapter;

    List<Integer> data0 = new LinkedList<>();

    public WelcomeViewHolder(IPlugin<Fragment, Integer> plugin) {
        super(plugin);
    }

    @Override
    public View install(Context context, Fragment fragment, ViewGroup parent) {
        rootView = LayoutInflater.from(context).inflate(R.layout.test_guide_welcome,parent,false);
        return rootView;
    }

    @Override
    public void notify0(int type, Integer key, IData iData) {
        switch (type){
            case IData.TYPE_INIT:
                if(iData == plugin.data()){
                    data0.add(R.mipmap.w1);
                    data0.add(R.mipmap.w2);
                    data0.add(R.mipmap.w3);

                    adapter.setData(data0);
                    vp.init(plugin.handler());
                }
                break;


        }
    }

    @Override
    public void init(Context context) {
        vp = rootView.findViewById(R.id.test_guide_welcome_vp);

        adapter = new CommonVPAdapter<Integer>(context) {
            @Override
            public View getView(ViewGroup container, Context context, Integer i) {
                ImageView iv = new ImageView(context);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                Glide.with(context).asDrawable().load(i).into(iv);

                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return iv;
            }
        };
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == 2){
                    vp.exit(plugin.handler());
                    plugin.proxy().proxy(GK.WELCOME_TO_HOME);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void exit(Context context) {
        vp.exit(plugin.handler());
    }
}
