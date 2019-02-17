package com.wsdc.g_a_0.plugin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wsdc.g_j_0.IPeriod0;

/*
 *  View的持有者
 *  <li>    activity
 *  <li>    fragment
 *  <li>    view
 *  <li>    T
 */
public interface IViewHolder<T> extends IPeriod0<Context>,IDataChangeListener<IData,Integer>{
    /*
     *  Fragment需要一个parent
     *  <li>    这个就是Activity中的容器View
     */
    View install(Context context, T t, ViewGroup parent);

    IData data();

    IProxy proxy();

    IPlugin plugin();

    T wrap();

    void uninstall();
}
