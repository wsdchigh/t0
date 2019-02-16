package com.wsdchigh.plugin_test;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.wsdc.g_a_0.BaseActivity;
import com.wsdc.g_a_0.ResourceProxy1;
import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.IProxy;
import com.wsdc.g_a_0.plugin.IViewHolder;

/*
 *  为了统一化
 *  <li>    所有的activity均继承自FragmentActivity
 *  <li>    requestWindowFeature(Window.FEATURE_NO_TITLE);
 *          <li>    去掉title (如果需要title，在布局中定义一个即可)
 */
public class MainActivity extends BaseActivity {

}
