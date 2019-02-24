package com.wsdc.myfresh;

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
     */
    boolean canFresh(int value);

    void complete();

    //CueSet getHeaders();

    //CueSet getTails();
    IExecutor executor();

    IData data();

    ICueDo header();

    ICueDo tail();
}
