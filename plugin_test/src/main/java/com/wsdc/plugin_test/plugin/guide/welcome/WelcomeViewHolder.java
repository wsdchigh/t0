package com.wsdc.plugin_test.plugin.guide.welcome;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;
import com.wsdc.plugin_test.GK;
import com.wsdc.plugin_test.R;

public class WelcomeViewHolder extends AbstractIViewHolder<Fragment> implements View.OnClickListener {
    Button btn;

    public WelcomeViewHolder(IPlugin<Fragment, Integer> plugin) {
        super(plugin);
    }

    @Override
    protected void clear() {

    }

    @Override
    public View install(Context context, Fragment fragment, ViewGroup parent) {
        rootView = LayoutInflater.from(context).inflate(R.layout.test_guide_welcome,parent,false);
        return rootView;
    }

    @Override
    public void notify0(int type, Integer key, IData iData) {

    }

    @Override
    public void init(Context context) {
        btn = rootView.findViewById(R.id.test_guide_welcome_btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void exit(Context context) {

    }

    @Override
    public void onClick(View v) {
        if(v == btn){
            proxy().proxy(GK.WELCOME_TO_HOME);
        }
    }
}