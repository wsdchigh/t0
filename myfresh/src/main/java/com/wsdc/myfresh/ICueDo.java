package com.wsdc.myfresh;

import android.view.View;

/*
 * Cue  提示，在刷新的过程之中，会显示头部或者尾部(header tail)
 *      >   针对View进行操作，可以是View实现接口或者实体类内部持有一个待操作的View
 *          >   推荐使用实现类持有一个View 便于安装和卸载操作
 *      >   对外部提供提示信息
 *      >   header tail 只会同事存在一个刷新
 *
 * <li>    ICueDo必须拥有一个构造桉树
 *          <li>    Context
 *          <li>    View
 *          <li>    反射模板需要
 */
public interface ICueDo {
    /*
     *  触发刷新，会调用一次该函数
     *  在滑动的过程之中，总的偏移数值 从 大于等于制定高度 ->   小于指定高度 也会触发这个函数
     */
    void pullMoreToFresh();

    /*
     *  在滑动的过程中 从小于指定高度 ->  大于等于指定高度  触发这个函数
     */
    void releaseToFresh();

    /*
     *  提供一个高级的刷新
     *  如果滑动的高度达到一个比较高的固定数值，触发一个额外的事件
     *      >   暂时没有想清楚后续该如何处理
     */
    boolean highLevelFresh();

    /*
     *  松手的时候，如果滑动的高度达到了指定的数值，触发这个函数
     */
    void loading();

    //  刷新完成
    void complete();

    //  取消
    void cancel();

    /*
     *  IFrame在偏移期间，会不停的触发这个函数，表达的是IFrame的偏移数值  (wx的大风车)
     */
    void frameOffset(int x,int y);

    /*
     *  手指松开之后，如果滑动的高度达到了固定的高度，ICue自身可用偏移定义的高度  (tt的新年快乐)
     *  需要借助Scroller或者View的post函数 一起完成这个功能
     */
    void offsetSelf(int x,int y);

    /*
     *  关闭资源函数
     *  <li>    如果
     */
    void close();

    View getView();

}
