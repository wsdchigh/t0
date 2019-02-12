package com.wsdc.g_a_0.plugin;

import android.os.Handler;
import android.os.Message;

import com.wsdc.g_a_0.router.IRouter;
import com.wsdc.g_j_0.IContainer0;
import com.wsdc.g_j_0.ILInk0;

/*
 *  插件
 *  <li>    插件
 *  <li>    插件配合路由一起工作
 *  <li>    插件可以脱离路由工作
 */
public interface IPlugin<T,K> extends ILInk0<IPlugin>,IContainer0<IPlugin,K> {
    /*
     *   任何一个插件，只有专门的服务功能
     *   <li>    activity,fragment 配合路由工作
     */
    public static final int WRAP_ACTIVITY = 1;
    public static final int WRAP_FRAGMENT = 2;
    public static final int WRAP_VIEW = 3;
    public static final int WRAP_DIALOG = 4;
    public static final int WRAP_POP_WINDOW = 5;
    public static final int WRAP_SERVICE = 6;
    public static final int WRAP_BROADCAST = 7;

    IData data();
    IProxy proxy();
    IViewHolder<T> viewHolder();
    IRouter router();

    //  获取服务的对象
    T wrap();

    //  唯一标识的key
    String key();

    //  如果插件不是底层插件  那么可以获取下一层处于工作的插件
    IPlugin current();

    //  提供一个Handler
    Handler handler();

    public boolean handleMessage(Message msg, IPlugin plugin0);
}
