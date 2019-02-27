package com.wsdc.myfresh.v1;

import android.view.MotionEvent;

/*
 *  刷新的几种状态
 *      >   spare       没有触发刷新状态
 *      >   scroll      进入滑动状态
 *      >   loading     进入加载状态
 *      >   complete    进入加载完成状态
 *
 *  刷新方向    (下拉 || 上拉)
 *      >   null        未定刷新方向
  *     >   down        下拉的刷新方向(取正值)
  *     >   up          上拉的刷新方向(取负值)
  *
  * 屏幕刷新方向
  *     >   null        没有任何刷新
  *     >   horizontal  横向
  *     >   vertical    纵向
  *
  * 滑动的来源
  *     >   null        当前状态没有发生任何滑动
  *     >   finger      当前是手指在引导界面进行滑动(实际可能没有发生任何滑动)
  *     >   auto        当前处于系统自动回滑状态
  *
  * 处于刷新状态期间的再次滑动界面
  *     >   null        期间没有发生再次滑动的操作
  *     >   fresh       期间发生了滑动操作
  *     >   fresh_complete  期间发生了滑动操作，并且在滑动期间，刷新已经完成了 故做出该标记
 */
public class IData {
    public static final int STATUS_SPARE = 0x1 ;
    public static final int STATUS_SCROLL = 0x1 << 1 ;
    public static final int STATUS_LOAD = 0x1 << 2 ;
    public static final int STATUS_COMPLETE = 0x1 << 3 ;

    public static final int DIRECTION_NULL = 0 ;
    public static final int DIRECTION_DOWN = 1;                 //
    public static final int DIRECTION_UP = -1;

    /*
     *  这三个状态不存在交叉
     *      >   null    屏幕上面没有任何滑动
     *      >   finger  手指在屏幕上面滑动
     *          >   只能从null切换到finger状态
     *          >
     *
     *      >   auto    系统自动回滑
     *          >   只能从null切换到auto状态
     */
    public static final int SCROLL_FINGER = 1;
    public static final int SCROLL_AUTO = -1;
    public static final int SCROLL_NULL = 0;

    public static final int LOAD_FRESH_NULL = 0x1;
    public static final int LOAD_FRESH = 0x1 << 1;
    public static final int LOAD_FRESH_COMPLETE = 0x1 << 2;

    public static final int SCREEN_NULL = 0 ;
    public static final int SCREEN_HORIONZAL = -1;
    public static final int SCREEN_VERTICAL = 1;

    /*
     *  信号 :    执行完来自frame的操作，给execute的一个回馈，来源于两个操作
     *      >   回滑结束分发一个信号
     *      >   加载完成分发一个信号
     */
    public static final int SEMAPHORE_NULL = 0;
    public static final int SEMAPHORE_LOAD = 1;
    public static final int SEMAPHORE_CANCEL = 2;
    public static final int SEMAPHORE_COMPLETE = 3;
    public static final int SEMAPHORE_LOAD_AGAIN = 4;

    /*
     *  status              标记整个刷新过程中的全局状态
     *  freshDirection      刷新的方向
     *  <li>    1   下拉
     *  <li>    -1  上拉
     *  scrollSource        当前的滑动来源
     *  loadAgainFresh      加载中的再次刷新状态
     */
    private int status;
    private int freshDirection;
    private int scrollSource;
    private int loadAgainFresh;

    private float curX;
    private float curY;
    private float lastX;
    private float lastY;

    private int offsetAll;
    private int offsetCurX;

    private int sem;
    private int returnTime = 250;

    private boolean isCompleted = false;

    //  记录刷新是横屏还是竖屏
    //  1 竖屏  -1 横屏
    public int direction;

    public float block_ratio;
    public float encourage_ratio;
    public boolean allow_dispatch;
    public int fresh_direction0;
    public boolean second_event;
    public boolean second_fresh;

    public int fresh_header_dimen;
    public int second_fresh_header_dimen;
    public int fresh_tail_dimen;

    public int fresh_header_max_dimen;
    public int fresh_tail_max_dimen;

    public boolean up_complete_type;
    public int return_time;

    public boolean use_default_i_cue;

    public IData() {
        recycler();
    }

    /*
     *  轨迹，记录偏移手指的偏移数值
     */
    private float orbit = 0;

    public void update(float x,float y){
        lastX = curX;
        lastY = curY;
        curX = x;
        curY = y;
    }

    public void recycler(){
        status = STATUS_SPARE;
        freshDirection = DIRECTION_NULL;
        scrollSource = SCROLL_NULL;
        loadAgainFresh = LOAD_FRESH_NULL;
        curX = 0;
        curY = 0;
        lastX = 0;
        lastY = 0;
        orbit = 0;
        sem = SEMAPHORE_NULL;
        isCompleted = false;
    }

    public int getStatus(){
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public int getOffsetAll(){
        return offsetAll * freshDirection;
    }

    public void setOffsetAll(int offsetAll){
        this.offsetAll = offsetAll;
    }

    public void setReturnTime(int returnTime){
        this.returnTime = returnTime;
    }

    public int getReturnTime(){
        return returnTime;
    }

    public void setIsCompleted(boolean isCompleted){
        this.isCompleted = isCompleted;
    }

    public boolean getIsCompleted(){
        return isCompleted;
    }

    /*
     *  计算偏移数值
     *      >   如果需要Frame进行偏移，调用这个函数获取偏移数值(如果没有触发刷新，调用这个函数没有意义)
     *      >   这个函数会及时的去修正当偏和总偏    (这个函数在每一次滑动的时候只需要调用一次，通常是需要调用一次的)
     *      >   不会因为滑动的轨迹过大而造成较大的偏移
     *      >   如果手指滑动期间造成总偏归零 需要将状态切换到spare
     *
     *      <li>    偏移一定不是负数  最小为0
     */
    public int offset(){
        calculateOffset();
        int tmp = (int)((orbit * 0.6f) * freshDirection);
        if(Math.abs(tmp) < 5){
            return 0;
        }else{
            if(Math.abs(tmp) > 20){
                tmp = tmp >= 0 ? 20:-20;
            }
            orbit = 0f;
            final int o1 = offsetAll + tmp;
            if(o1 > 0){
                offsetAll += tmp;
                return tmp * freshDirection;
            }else{
                final int offsetTmp = -offsetAll * freshDirection;
                offsetAll = 0;
                return offsetTmp;
            }
        }

    }

    /*
     *  当status处于spare状态的时候，每一次滑动均会调用这个函数进行一次判定，来决定是否需要切换到scroll状态
     */
    public int getEventDirection(){
        if(curY - lastY > 0){
            return DIRECTION_DOWN;
        }else if(curY - lastY == 0){
            return DIRECTION_NULL;
        }else {
            return DIRECTION_UP;
        }
    }

    /*
     *  手指的方向
     */
    public int getFingerDirectionInScreen(){
        float fa = Math.abs(lastX-curX);
        float fb = Math.abs(lastY-curY);
        if(fa == fb){
            return 0;
        }
        return fa>fb?-1:1;
    }

    public void setFreshDirection(int freshDirection){
        this.freshDirection = freshDirection;
    }

    public int getFreshDirection(){
        return freshDirection;
    }

    public void setLoadAgainFresh(int loadAgainFresh){
        this.loadAgainFresh = loadAgainFresh;
    }

    public int getLoadAgainFresh(){
        return loadAgainFresh;
    }

    public void setScrollSource(int scrollSource){
        this.scrollSource = scrollSource;
    }

    public int getScrollSource(){
        return scrollSource;
    }

    public void setSemaphore(int sem){
        this.sem = sem;
    }

    public int getSemaphore(){
        return sem;
    }

    private void calculateOffset(){
        orbit += (curY - lastY);
        //Log.d("wsdc", "cur = "+curY+"   last = "+lastY+"    orbit = "+orbit);
    }

    float x1,y1;
    float ox1,oy1;
    public void down(MotionEvent ev) {
        x1 = ev.getX();
        y1 = ev.getY();

        ox1 = 0;
        oy1 = 0;
    }

    public void move(MotionEvent ev){
        float x2 = ev.getX();
        float y2 = ev.getY();

        ox1 += x2 - x1;
        oy1 += y2 - y1;
    }

    /*
     *  解决横向纵向滑动的冲突问题
     */
    public int get(){
        int k1 = (int) Math.abs(ox1);
        int k2 = (int) Math.abs(oy1);
        if(k1 > 20 || k2 > 20){
            if(k1 >= k2){
                return -1;
            }else{
                return 1;
            }
        }else{
            return 0;
        }
    }

}
