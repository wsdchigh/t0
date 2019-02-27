package com.wsdc.myfresh;

import android.view.MotionEvent;

import com.wsdc.myfresh.v1.IData;

/**
 * Created by wsdchigh on 2018/1/15.
 */

public interface IFrameDo {
    public static final int STATUS_NULL = 0;
    public static final int STATUS_HEADER = 1;
    public static final int STATUS_TAIL = -1;

    /*
     *  从刷新状态到非刷新之间的切换  调用这个函数调整布局
     */
    void freshChanged(int status);

    /*
     *  能否刷新
     *      >   ViewGroup.canScrollVertical(int value)
     *          >   负数 是否能够继续往下滑动
     *
     *  <li>    如果事件在一个指定的范围里面
     */
    boolean canFresh(int value, MotionEvent ev);

    void complete();

    //CueSet getHeaders();

    //CueSet getTails();
    IExecutor executor();

    IData data();

    ICueDo header();

    ICueDo tail();

    void setFrameExpand(FrameExpand expand);

    public interface FrameExpand{
        /*
         *  在触发刷新的时候，需要参考事件的位置是否需要拦截
         *  <li>    如果子View需要拦截事件(滑动)，如果子View滑动需要滑动
         */
        boolean intercept(MotionEvent ev);
    }
}
