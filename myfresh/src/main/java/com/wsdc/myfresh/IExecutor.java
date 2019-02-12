package com.wsdc.myfresh;

import android.view.MotionEvent;

/*
 *  处理中心，联合所有逻辑，在这里统一处理
 *      >   获取从IFrame下发的坐标
 *      >   从IData获取数据
 *      >   决定数据如何下发
 */
public interface IExecutor {
    public static final int ADD_HEADER = 1;
    public static final int REMOVE_HEADER = 2;
    public static final int ADD_TAIL = 3;
    public static final int REMOVE_TAIL = 4;

    boolean dispatchTouchEvent(MotionEvent ev);

    void sem(int semaphore);

    void addFreshListener(FreshListener listener);

    /*
     *  操作cue
     *  <li>    增加header
     *  <li>    移除header
     *  <li>    增加tail
     *  <li>    移除tail
     */
    void operateCue(int type,ICueDo cue);

    /*
     *  如果调用    Scroller类用来自己滑动，需要将函数代理给 IExecutor
     *  <li>    决定滑动应该由谁来滑动
     *  <li>    frame可能需要偏移到0
     *  <li>    如果frame不在处理滑动，此时模拟15个滑动事件，驱动content进行滑动
     *          <li>    此时，content自己可以处理这些滑动
     */
    void computedScroll();

    /*
     *  考虑到header会显示的情况
     *  <li>    如果因为惯性滑动到顶部，此时header需要画出来
     *  <li>    使用监听滑动的方式获取  速率，当content滑动到顶的时候  使用这个速率继续便宜
     */
    void scrollToHeaderAndContinue(float velocity);
}
