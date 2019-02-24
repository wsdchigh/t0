package com.wsdc.plugin_test.plugin.guide.guide0;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;
import com.wsdc.plugin_test.R;
import com.wsdc.plugin_test.GK;

/*
 *  引导页面
 *  <li>    如果这是第一次使用(或者清空了数据)，那么3s之后会进入到欢迎界面
 *          <li>    反之，进入到首页
 *
 *  <li>    该页面不保存在路由之中
 */
public class Guide0ViewHolder extends AbstractIViewHolder<Fragment>{
    private Context context;
    public Guide0ViewHolder(IPlugin<Fragment, Integer> plugin) {
        super(plugin);
    }

    @Override
    public View install(Context context, Fragment fragment, ViewGroup parent) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.test_guide_guide0,parent,false);
        return rootView;
    }

    @Override
    public void notify0(int type, Integer key, IData iData) {

    }

    @Override
    public void init(Context context) {
        rootView.setBackgroundResource(R.mipmap.guide);

        //  让全局插件去处理，因为需要全局插件中的数据来执行操作
        Starter.getInstance().globalPlugin().proxy().proxy(GK.GUIDE0_GO);
    }

    @Override
    public void exit(Context context) {

    }
}
