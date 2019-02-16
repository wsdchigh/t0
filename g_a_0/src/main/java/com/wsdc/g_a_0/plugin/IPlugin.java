package com.wsdc.g_a_0.plugin;

import android.content.Context;
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
    public static final int STATUS_LEVEL_MASK = STATUS_BASE1;

    public static final int LEVEL_1 = STATUS_BASE;
    public static final int LEVEL_2 = STATUS_BASE << 1;

    /*
     *  启动模式
     *  <li>    保存父插件的启动参数
     *  <li>    保存当前插件的启动参数
     *  <li>    启动参数2位  4个值
     *
     *  <li>    父插件不是主动启动的，而是根据子插件，因为父插件不存在而生成
     *          <li>    父插件如果不存在，根据子插件的parent参数进行构建
     */
    public static final int STATUS_START_PARENT_MODE_MASK = STATUS_BASE1 << 2;
    public static final int STATUS_START_SELF_MODE_MASK = STATUS_BASE1 << 4;

    /*
     *  具体的启动模式 (暂时设定两种)
     *  <li>    正常入栈
     *  <li>    不入栈
     *
     *  <li>    status取结果的时候，需要右偏移才能比对结果
     */
    public static final int START_COMMON = 0x0;
    public static final int START_NOT_STACK = 0x1 << 4;

    //  plugin有个安装参数
    public static final int STATUS_INSTALL_MASK = STATUS_BASE << 5;

    //  没有安装  可以安装
    public static final int STATUS_INSTALL_NOT = 0;

    //  已经安装，不需要安装
    public static final int STATUS_INSTALL_YES = STATUS_INSTALL_MASK;


    IData data();
    IProxy proxy();
    IViewHolder<T> viewHolder();
    IRouter router();

    /*
     *  插件的安装和卸载
     *  <li>    我们保证activity栈中只存在1个Activity
     *  <li>    使用独立的表存储插件路由信息
     *  <li>    我们在create的时候创建路由，destroy的时候卸载路由
     *
     *  <li>    卸载并不代表，数据的回收
     */
    void install(Context context,int container_id,T t);

    void uninstall();

    //  获取服务的对象
    //  在没有安装期间  Activity Service是空的  不能调用这个函数去执行跳转
    //  根据 wrapKey 获取对应的路径
    T wrap();

    //  唯一标识的key
    String key();

    //  如果插件不是底层插件  那么可以获取下一层处于工作的插件
    IPlugin current();

    //  提供一个Handler
    Handler handler();

    boolean handleMessage(Message msg, IPlugin plugin0);

    /*
     *  一个32位的状态值，记录状态信息
     */
    int status();

    void updateStatus(int newStatus);

    /*
     *  如果是fragment，需要一个fragment的ID
     */
    int id();

    /*
     *   如果是Activity+Fragment的组合路由
     *   <li>    Activity需要一个ID，用于添加Fragment
     *   <li>    R.id.container     这是一个Activity的View的ID，用于填充Fragment使用
     *   <li>    这里直接保存为一个数字就好了
     */
    int childLayout();

    /*
     *  所处的APK位置
     */
    APK apk();

    /*
     *  系统在运行的过程中保存的信息
     *  <li>    保存数据使用  保存数据 保存数据 保存数据
     */
    void setTag(Object o);

    Object getTag();
}
