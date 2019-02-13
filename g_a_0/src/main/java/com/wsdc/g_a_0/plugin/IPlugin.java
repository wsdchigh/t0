package com.wsdc.g_a_0.plugin;

import android.os.Handler;
import android.os.Message;

import com.wsdc.g_a_0.APK;
import com.wsdc.g_a_0.router.IRouter;
import com.wsdc.g_j_0.IContainer0;
import com.wsdc.g_j_0.ILInk0;

/*
 *  插件
 *  <li>    插件
 *  <li>    插件配合路由一起工作
 *  <li>    插件可以脱离路由工作
 *
 *  <li>    如果插件是配合路由一起工作的，我们期待
 *          <li>    路由只有两层
 *          <li>    顶层是Activity
 *          <li>    二层是Fragment
 *          <li>    Activity充当容器，Fragment负载功能
 *                  <li>    Activity可以有部分功能
 *
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

    //  占1位
    public static final int STATUS_BASE = 0x1;
    //  占2位
    public static final int STATUS_BASE1 = 0x3;
    //  占3位
    public static final int STATUS_BASE2 = 0x7;
    //  占4位
    public static final int STATUS_BASE3 = 0xe;

    //  使用两位保存level信息   从 1 开始
    public static final int STATUS_LEVEL = STATUS_BASE1;

    /*
     *  启动模式
     *  <li>    保存父插件的启动参数
     *  <li>    保存当前插件的启动参数
     *  <li>    启动参数2位  4个值
     *
     *  <li>    父插件不是主动启动的，而是根据子插件，因为父插件不存在而生成
     *          <li>    父插件如果不存在，根据子插件的parent参数进行构建
     */
    public static final int STATUS_START_PARENT_MODE = STATUS_BASE1 << 2;
    public static final int STATUS_START_SELF_MODE = STATUS_BASE1 << 4;

    /*
     *  具体的启动模式 (暂时设定两种)
     *  <li>    正常入栈
     *  <li>    不入栈
     *
     *  <li>    status取结果的时候，需要右偏移才能比对结果
     */
    public static final int START_COMMON = 0x0;
    public static final int START_NOT_STACK = 0x1;

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

    /*
     *  一个32位的状态值，记录状态信息
     */
    int status();

    /*
     *  如果是fragment，需要一个fragment的ID
     */
    int id();

    /*
     *  所处的APK位置
     */
    APK apk();
}
