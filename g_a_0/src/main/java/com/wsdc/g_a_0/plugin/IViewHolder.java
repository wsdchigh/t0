package com.wsdc.g_a_0.plugin;

import android.content.Context;
import android.view.View;

import com.wsdc.g_j_0.IPeriod0;

/*
 *  View的持有者
 *  <li>    activity
 *  <li>    fragment
 *  <li>    view
 *  <li>    T
 */
public interface IViewHolder<T> extends IPeriod0<Context>{
    View install(Context context,T t);

    IData data();

    IProxy proxy();

    IPlugin plugin();

    T wrap();
}
