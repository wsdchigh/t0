package com.wsdc.myfresh.v1;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.wsdc.myfresh.FreshListener;
import com.wsdc.myfresh.ICueDo;
import com.wsdc.myfresh.IExecutor;
import com.wsdc.myfresh.cue.CueSet;

import java.util.LinkedList;
import java.util.List;

public class Executor implements IExecutor {
    private IData data;
    private IFrame frame;
    private List<FreshListener> listenersContainer = new LinkedList<>();
    private int returnTime;

    /*
     *  frame 驱动的 view
     *  <li>    scrollBy
     *  <li>    invalid
     */
    private View frameView;
    private Scroller scroller;

    private CueSet headerSet = new CueSet();
    private CueSet tailSet = new CueSet();

    //  标记是否下发了了down事件
    private boolean dispatchDown = false;

    int k = 0;

    public Executor(IFrame frame) {
        this.frame = frame;
        data = new IData();
        returnTime = data.getReturnTime();
    }


    public Executor(IFrame frame, View view, Context context,IData data) {
        this.frame = frame;
        this.data = data;
        this.frameView = view;
        scroller = new Scroller(context);
        returnTime = data.getReturnTime();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int status = data.getStatus();
        data.update(ev.getX(),ev.getY());

        int offset;
        int offsetAll;
        int scrollSource;

        /*
         *  针对事件的类型 + 刷新的状态码 两层嵌套，综合分析
         */
        switch (ev.getAction()){
            /*
             *  down事件
             *      >   只在没有任何滑动的情况之下接受事件，并且向frame分发事件
             *      >   将滑动状态切换到 finger状态
             */
            case MotionEvent.ACTION_DOWN:
                //  回滑期间  不接受任何事件
                if(data.getScrollSource() == IData.SCROLL_NULL){
                    data.setScrollSource(IData.SCROLL_FINGER);
                    frame.superDispatchEvent(ev);
                    dispatchDown = true;
                }

                data.down(ev);
                k = 0;

                break;

            /*
             *  move事件
             *      >   spare周期
             *          >   判定是否需要刷新
             *              >   需要刷新
             *                  >   取消之前分发给frame的down事件
             *                  >   设置刷新方向
             *                  >   将状态切换到scroll
             *                  >   frame刷新布局
             *                  >   获取偏移数值进行偏移
             *
             *              >   不需要刷新
             *                  >   frame分发事件
             */
            case MotionEvent.ACTION_MOVE:
                //  系统回滑期间  不接受滑动事件
                if(data.getScrollSource() == IData.SCROLL_AUTO){
                    return true;
                }

                /*
                 *  滑动期间，在判断的时候，拦截最多10px
                 *  <li>    从down事件开始到move事件，期间会累计x,y的偏移，直到x,y的偏移值超过10px
                 *          低于这个值得时候，move事件全部拦截掉，不做任何处理
                 *
                 *  <li>    之所以设定这个阈值
                 *          <li>    确定是横向滑动还是纵向滑动
                 *          <li>    确定滑动的方向(正方向?负方向)
                 *
                 *  <li>    这个机制针对于spare周期的拦截处理
                 *
                 *  <li>    这里屏蔽掉横向滑动
                 */

                int dir = data.getEventDirection();

                //  避开 down和scroll处在同一个点的位置
                if(dir == IData.DIRECTION_NULL){
                    break;
                }

                switch (status){
                    /*
                     *  spare周期是可能存在偏移的 (上拉刷新完成之后，通常是存在偏移的)
                     *  <li>    如果有偏移，优先考虑偏移
                     */
                    case IData.STATUS_SPARE:
                        if(k == 0){
                            data.move(ev);
                            k = data.get();

                            if(k == 0){
                                break;
                            }
                        }

                        if(k == -1){
                            frame.superDispatchEvent(ev);
                            break;
                        }

                        offsetAll = data.getOffsetAll();
                        if(offsetAll == 0){
                            if(frame.canFresh(dir,ev)){
                                //  暂时禁止上拉  事件下发下去
                                if(dir < 0){
                                    frame.superDispatchEvent(ev);
                                    break;
                                }
                                cancelDown(ev);
                                data.setFreshDirection(dir);
                                data.setStatus(status << 1);
                                offset = data.offset();
                                frame.freshChanged(dir);
                                if(offset != 0){
                                    //frame.offset(0,-offset);
                                    offset(0,-offset);
                                }

                                //  给出刷新通知
                                if(dir == IData.DIRECTION_DOWN){
                                    headerSet.pullMoreToFresh();
                                }else{
                                    tailSet.pullMoreToFresh();
                                }
                            }else{
                                frame.superDispatchEvent(ev);
                            }
                        }else{
                            /*
                             *  上拉刷新期间允许有一定的偏移
                             */
                            offset = data.offset();
                            //frame.offset(0,-offset);
                            offset(0,-offset);
                        }

                        break;

                    /*
                     *  滑动周期
                     *      >   触发了滑动之后，将不再分发down事件
                     *      >   逆向刷新方向的滑动 可以将周期从scroll切换到spare周期
                     */
                    case IData.STATUS_SCROLL:
                        final int originOffsetAll = data.getOffsetAll();
                        offset = data.offset();
                        final int nowOffsetAll = data.getOffsetAll();
                        if(offset != 0){
                            offset(0,-offset);

                            /*
                             *  如果偏移前后跨过了刷新线
                             *  <li>    分方向判断
                             */
                            if(data.getFreshDirection() == IData.DIRECTION_DOWN){
                                if(originOffsetAll < data.fresh_header_dimen && nowOffsetAll >= data.fresh_header_dimen){
                                    headerSet.releaseToFresh();
                                }

                                if(originOffsetAll >= data.fresh_header_dimen && nowOffsetAll < data.fresh_header_dimen){
                                    tailSet.releaseToFresh();
                                }

                                headerSet.offsetSelf(0,-offset);
                            }else{
                                if(originOffsetAll < data.fresh_tail_dimen && nowOffsetAll >= data.fresh_tail_dimen){
                                    headerSet.pullMoreToFresh();
                                }

                                if(originOffsetAll >= data.fresh_tail_dimen && nowOffsetAll < data.fresh_tail_dimen){
                                    tailSet.pullMoreToFresh();
                                }
                                tailSet.offsetSelf(0,-offset);
                            }

                            if(data.getOffsetAll() == 0){
                                //  调用这个函数
                                if(data.getFreshDirection() == IData.DIRECTION_DOWN){
                                    headerSet.close();
                                }else{
                                    tailSet.close();
                                }

                                data.setStatus(IData.STATUS_SPARE);
                                data.recycler();
                                data.update(ev.getX(),ev.getY());

                                //  刷新布局
                                frame.freshChanged(data.getFreshDirection());
                            }
                        }


                        //  二级刷新事件
                        break;

                    /*
                     *  二次事件
                     */
                    case IData.STATUS_COMPLETE:
                    case IData.STATUS_LOAD:
                        offsetAll = data.getOffsetAll();
                        scrollSource = data.getScrollSource();
                        if(offsetAll == 0){
                            //Log.d("wsdc", "fresh = "+(frame.canFresh(dir))+"    dir = "+dir);
                            //  如果滑动和刷新相反  那么需要content去处理数据
                            if(dir != data.getFreshDirection() || !frame.canFresh(dir,ev)){
                                dispatchDownMotionEvent(ev);
                                frame.superDispatchEvent(ev);
                                // Log.d("wsdc", "o1 = "+dir+"     o2 = "+data.getFreshDirection());
                                return true;
                            }
                        }else if(scrollSource == IData.SCROLL_NULL){
                            data.setScrollSource(IData.SCROLL_FINGER);
                        }
                        offset = data.offset();
                        if(offset != 0){
                            offset(0,-offset);
                        }
                        //Log.d("wsdc", "offsetALL = "+data.getOffsetAll()+"  origin = "+offsetAll+"      "+ frameView.getScrollY()+"     offset = "+offset);

                        //  取消发送down事件
                        cancelDown(ev);
                        break;
                }

                break;

            /*
             *  松手事件
             *     >    通常需要将滑动标记为null或者auto
             */
            case MotionEvent.ACTION_UP:
                if(data.getScrollSource() == IData.SCROLL_AUTO){
                    return true;
                }
                switch (status){
                    /*
                     *  spare周期
                     *      >   frame分发事件
                     *      >   滑动标记为null
                     */
                    case IData.STATUS_SPARE:
                        frame.superDispatchEvent(ev);
                        data.setScrollSource(IData.SCROLL_NULL);
                        break;

                    /*
                     *  scroll周期
                     *      >   根据总偏进行回滑
                     *          >   小于180 视为失败，直接返回原点
                     *          >   大于180 看做成功，滑动到180这个点
                     *      >   标记回滑信号
                     *          >   load
                     *          >   cancel
                     *      >   标记滑动状态
                     *          >   标记为auto
                     */
                    case IData.STATUS_SCROLL:
                        offsetAll = data.getOffsetAll();
                        if(Math.abs(offsetAll) > 180){
                            scrollBack(0,-offsetAll,0,-180 * data.getFreshDirection()+offsetAll,returnTime);
                            data.setOffsetAll(180);
                            data.setSemaphore(IData.SEMAPHORE_LOAD);

                            /*
                             *  loading阶段
                             *  <li>    暂时不通知
                             *  <li>    等掉下来之后再行通知
                             */
                            /*
                            if(data.getFreshDirection() == IData.DIRECTION_DOWN){
                                headerSet.loading();
                            }else{
                                tailSet.loading();
                            }
                            */
                        }else{
                            scrollBack(0,-offsetAll,0,offsetAll,returnTime);
                            data.setOffsetAll(0);
                            data.setSemaphore(IData.SEMAPHORE_CANCEL);

                            if(data.getFreshDirection() == IData.DIRECTION_DOWN){
                                headerSet.cancel();
                            }else{
                                tailSet.cancel();
                            }
                        }
                        data.setScrollSource(IData.SCROLL_AUTO);
                        break;

                    /*
                     *  load周期
                     *      >   如果offsetAll == 0 直接将滑动标记为 null
                     *      >   如果offsetAll > 180 开启回滑，回滑到180
                     *          >   信号标记为load_again
                     *          >   滑动标记为auto
                     *      >   如果offsetAll > 0 < 180 开启回滑 回滑到 0
                     *          >   信号标记为load_again
                     *          >   滑动标记为auto
                     */
                    case IData.STATUS_LOAD:
                        offsetAll = data.getOffsetAll();
                        if(offsetAll == 0){
                            data.setScrollSource(IData.SCROLL_NULL);
                        }else{
                            if(Math.abs(offsetAll) > 180){
                                scrollBack(0,-offsetAll,0,-180 * data.getFreshDirection()+offsetAll,returnTime);
                                data.setOffsetAll(180);
                            }else{
                                scrollBack(0,-offsetAll,0,offsetAll,returnTime);
                                data.setOffsetAll(0);
                            }
                            data.setSemaphore(IData.SEMAPHORE_LOAD_AGAIN);
                            data.setScrollSource(IData.SCROLL_AUTO);
                        }

                        //  下发up事件
                        frame.superDispatchEvent(ev);
                        break;

                    /*
                     *  complete周期
                     *      >   此时已经刷新完成了，直接回滑到0这个位置即可
                     *          >   offsetAll== 0   直接cancel到初始状态
                     *          >   offsetAll > 0   回滑到零点
                     *              >
                     */
                    case IData.STATUS_COMPLETE:
                        offsetAll = data.getOffsetAll();
                        if(offsetAll == 0){
                            data.recycler();
                        }else{
                            scrollBack(0,-offsetAll,0,offsetAll,returnTime);
                            data.setOffsetAll(0);
                            data.setSemaphore(IData.SEMAPHORE_CANCEL);
                            data.setScrollSource(IData.SCROLL_AUTO);
                        }

                        //
                        frame.superDispatchEvent(ev);
                        break;
                }
                break;

            default:
                frame.superDispatchEvent(ev);
        }

        return true;
    }

    private void scrollBack(int startX, int startY, int offsetX, int offsetY, int time) {
        scroller.startScroll(startX,startY,offsetX,offsetY,time);
        frameView.invalidate();
    }

    @Override
    public void computedScroll() {
        if(scroller.computeScrollOffset()){
            frameView.scrollTo(scroller.getCurrX(),scroller.getCurrY());
            frameView.invalidate();
            if(scroller.isFinished()){
                sem(-1);
            }
        }
    }

    private void offset(int x,int y){
        frameView.scrollBy(x,y);
        frameView.invalidate();
    }

    /*
     *  信号处理函数，处理来源
     *      >   手指松开造成的回滑，回滑结束之后调用信号函数
     *      >   外部表示加载完成
     */
    @Override
    public void sem(int semaphore){
        int scrollSource;
        int freshDirection;
        int status;
        int offsetAll;
        if(semaphore == IData.SEMAPHORE_COMPLETE){
            scrollSource = data.getScrollSource();
            data.setIsCompleted(true);
            data.setStatus(IData.STATUS_COMPLETE);
            if(scrollSource == IData.SCROLL_NULL){
                offsetAll = data.getOffsetAll();
                scrollBack(0,-offsetAll,0,offsetAll,returnTime);
                data.setOffsetAll(0);
                data.setSemaphore(IData.SEMAPHORE_CANCEL);
                data.setScrollSource(IData.SCROLL_AUTO);
            }

            //  显示完成
            if(data.getFreshDirection() == IData.DIRECTION_DOWN){
                headerSet.complete();
            }else{
                tailSet.complete();
            }

            return ;
        }

        int sem = data.getSemaphore();

        switch (sem){
            /*
             *  激活刷新的第一次回滑 调用这个信号
             *      >   切换到load周期
             *      >   消除信号
             *      >   标记滑动为 null
             */
            case IData.SEMAPHORE_LOAD:
                freshDirection = data.getFreshDirection();
                if(freshDirection == IData.DIRECTION_DOWN){
                    notifyFresh(1);
                    headerSet.loading();
                }else if(freshDirection == IData.DIRECTION_UP){
                    notifyFresh(-1);
                    tailSet.loading();
                }
                data.setStatus(IData.STATUS_LOAD);
                data.setSemaphore(IData.SEMAPHORE_NULL);
                data.setScrollSource(IData.SCROLL_NULL);
                break;

            /*
             *  回归到spare的回滑均调用这个函数
             *      >   未成功激活刷新的取消回滑
             *      >   complete周期的回滑
             *
             *      >   spare周期的数据回收
             *      >   frame刷新布局
             */
            case IData.SEMAPHORE_CANCEL:
                data.recycler();
                frame.freshChanged(IData.DIRECTION_NULL);
                break;

            /*
             *  刷新期间的滑动触发的回滑信号
             *      >   标记滑动为null
             *      >   清空信号
             *      >   如果回滑期间触发了刷新完成,这里将需要一次complete回滑
             *          >   开启回滑
             *          >   标记信号 cancel
             *          >   标记滑动 auto
             */
            case IData.SEMAPHORE_LOAD_AGAIN:
                status = data.getStatus();
                data.setScrollSource(IData.SCROLL_NULL);
                data.setSemaphore(IData.SEMAPHORE_NULL);
                if(status == IData.STATUS_COMPLETE){
                    offsetAll = data.getOffsetAll();
                    scrollBack(0,-offsetAll,0,offsetAll,returnTime);
                    data.setOffsetAll(0);
                    data.setSemaphore(IData.SEMAPHORE_CANCEL);
                    data.setScrollSource(IData.SCROLL_AUTO);
                }
                break;
        }
    }

    @Override
    public void addFreshListener(FreshListener listener) {
        listenersContainer.add(listener);
    }

    @Override
    public void operateCue(int type, ICueDo cue) {
        switch (type){
            case ADD_HEADER:
                headerSet.add(cue);
                break;

            case REMOVE_HEADER:
                headerSet.remove(cue);
                break;

            case ADD_TAIL:
                tailSet.add(cue);
                break;

            case REMOVE_TAIL:
                tailSet.remove(cue);
                break;
        }
    }

    @Override
    public void scrollToHeaderAndContinue(float velocity) {

    }

    //  通知所有的观察者  fresh == 1 down  fresh == -1 up
    private void notifyFresh(int fresh){
        if(listenersContainer == null){
            return;
        }

        for (FreshListener freshListener : listenersContainer) {
            if(fresh == 1){
                freshListener.pullToFresh();
            }else{
                freshListener.loadMore();
            }
        }
    }


    private MotionEvent dispatchCancelMotionEvent(MotionEvent ev){
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(MotionEvent.ACTION_CANCEL);
        return cancel;
    }

    private void dispatchDownMotionEvent(MotionEvent ev){
        if(!dispatchDown){
            MotionEvent down = MotionEvent.obtain(ev);
            down.setAction(MotionEvent.ACTION_DOWN);
            frame.superDispatchEvent(down);
            dispatchDown = true;

            //Log.d("wsdc", "下发down事件");
        }

    }

    private void cancelDown(MotionEvent ev){
        if(dispatchDown){
            dispatchDown = false;
            frame.superDispatchEvent(dispatchCancelMotionEvent(ev));
        }
    }
}
