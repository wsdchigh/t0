package com.wsdc.myfresh.v1;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.wsdc.myfresh.ICueDo;
import com.wsdc.myfresh.IExecutor;
import com.wsdc.myfresh.IFrameDo;
import com.wsdc.myfresh.R;
import com.wsdc.myfresh.cue.DefaultHeader;
import com.wsdc.myfresh.cue.DefaultTail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/*
 *  刷新框架
 *      >   将需要刷新的内容使用这个充当父容器
 *
 *  功能函数
 *      >   scrollTo()  调控自身的偏移
 *      >   Scroller    计算松手之后 Frame 自身的偏移
 *      >   手指滑动的时候，根据手指的滑动轨迹，计算出Frame的偏移
 *
 *  功能组件
 *      >   Header  一个位于顶部隐藏的ICue
 *      >   Content 我们需要刷新的内容体
 *      >   Tail    一个位于顶部隐藏的ICue
 *
 *      >   出于通用的逻辑考虑，下拉刷新显示Header，所以需要在顶部添加一个Header
 *          上拉加载更多显示Tail 所以需要在底部添加一个Tail
 *          默认的情况之下是有两个通用的ICue
 *      >   支持多个ICue，注册在合适的位置即可
 *
 *
 *  <li>    此处只适合完全隐藏cue的View
 */
public class IFrame extends ViewGroup implements IFrameDo {
    private Scroller scroller;
    private int status = IFrame.STATUS_NULL;
    private IExecutor executor;
    private View content0;
    private ICueDo header0,tail0;
    private IData data;


    public IFrame(Context context) {
        this(context,null);
    }

    public IFrame(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        data = new IData();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IFrame, 0, 0);
        //  初始化方向
        String direction = ta.getString(R.styleable.IFrame_direction);
        data.direction = direction == null?1:"vertical".equals(direction)?1:-1;

        float block_ratio = ta.getFloat(R.styleable.IFrame_block_ratio, 0.7f);
        data.block_ratio = block_ratio >0.8f?0.8f:block_ratio<0.2f?0.2f:block_ratio;

        float encourage_ratio = ta.getFloat(R.styleable.IFrame_encourage_ratio,1.2f);
        data.encourage_ratio = encourage_ratio>1.2f?1.2f:encourage_ratio<1.0f?1.0f:encourage_ratio;

        //  无论是否处理成功 是否需要下发事件
        data.allow_dispatch = ta.getBoolean(R.styleable.IFrame_allow_dispatch,false);

        //  允许的支持方向
        data.fresh_direction0 = ta.getInt(R.styleable.IFrame_fresh_direction,3);
        data.fresh_direction0 = data.fresh_direction0<1||data.fresh_direction0>3?3:data.fresh_direction0;

        //  是否支持二次事件
        data.second_event = ta.getBoolean(R.styleable.IFrame_second_event,true);

        //  是否支持二级刷新
        data.second_fresh = ta.getBoolean(R.styleable.IFrame_second_fresh,false);

        //  触发头部刷新的偏移
        data.fresh_header_dimen = ta.getInt(R.styleable.IFrame_fresh_header_dimen,200);
        //  触发二级刷新的偏移
        data.second_fresh_header_dimen = ta.getInt(R.styleable.IFrame_second_fresh_header_dimen,400);
        //  触发底部刷新的偏移
        data.fresh_tail_dimen = ta.getInt(R.styleable.IFrame_fresh_tail_dimen,200);
        //  上拉刷新之后，是否需要回弹
        data.up_complete_type = ta.getBoolean(R.styleable.IFrame_up_complete_type,true);
        data.return_time = ta.getInt(R.styleable.IFrame_return_time,250);

        //  是否使用默认的设置
        data.use_default_i_cue = ta.getBoolean(R.styleable.IFrame_use_default_i_cue,true);

        ta.recycle();
        scroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            LayoutParams params0 = child.getLayoutParams();
            if(params0 instanceof LayoutParams0){
                Log.d("wsdc",((LayoutParams0) params0).type);
                LayoutParams0 params1 = (LayoutParams0) params0;
                if(params1 == null){
                    continue;
                }

                if("header".equals(params1.type)){
                    if(header0 == null){
                        ICueDo i0 = get(params1.cueClassName, getContext(), child);
                        if(i0 == null){
                            continue;
                        }
                        header0 = i0;
                    }
                }

                if("tail".equals(params1.type)){
                    if(tail0 == null){
                        ICueDo i0 = get(params1.cueClassName, getContext(), child);
                        if(i0 == null){
                            continue;
                        }
                        tail0 = i0;
                    }
                }

                if("content".equals(params1.type)){
                    if(content0 == null){
                        content0 = child;
                    }
                }
            }
        }

        //  如果刷新View的时候  需要设置默认的初始值
        //  如果允许上拉刷新，但是没有设置header，这是没有问题的
        if(data.use_default_i_cue){
            if(((data.fresh_direction0 & 1) ==1) && header0 == null){
                header0 = new DefaultHeader(getContext(),this);
                addView(header0.getView());
            }

            if(((data.fresh_direction0 & 2) == 2) && tail0 == null){
                tail0 = new DefaultTail(getContext(),this);
                addView(tail0.getView());
            }
        }

        if(content0 == null){
            throw new RuntimeException("must have a content");
        }

        executor = new Executor(this,this,getContext(),data);

        if(header0 != null){
            executor.operateCue(IExecutor.ADD_HEADER,header0);
        }

        if(tail0 != null){
            executor.operateCue(IExecutor.ADD_TAIL,tail0);
        }


    }


    private ICueDo get(String name,Context context,View view){
        try {
            Class<?> clz = Class.forName(name);
            Constructor<?> constructor = clz.getConstructor(Context.class, View.class);
            Object o = constructor.newInstance(context, view);
            if(o instanceof ICueDo){
                return (ICueDo) o;
            }
            //return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("hfeiwue");
    }

    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        int width = 0;
        int height = 0;
        View view = null;
        switch (status){
            case IFrameDo.STATUS_NULL:

                width = content0.getMeasuredWidth();
                height = content0.getMeasuredHeight();
                content0.layout(0,0,width,height);
                break;

            case IFrame.STATUS_HEADER:
                view = header0.getView();
                view.layout(0,-view.getMeasuredHeight(),view.getMeasuredWidth(),0);

                width = content0.getMeasuredWidth();
                height = content0.getMeasuredHeight();
                content0.layout(0,0,width,height);
                break;

            case IFrame.STATUS_TAIL:
                view = tail0.getView();

                width = content0.getMeasuredWidth();
                height = content0.getMeasuredHeight();
                content0.layout(0,0,width,height);

                view.layout(0,height,view.getMeasuredWidth(),height+view.getMeasuredHeight());
                break;
        }

    }

    public boolean superDispatchEvent(MotionEvent ev){
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(executor != null){
            return executor.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        /*
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
            if(scroller.isFinished()){
                executor.sem(-1);
            }
        }
        */
        executor.computedScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        int childState = 0 ;
        switch (status){
            /*
             *  情景1
             *  <li>    只显示content，不显示cue(当前容器包含)
             *
             *
             *  情景2
             *  <li>    content和header是并存的  (微信朋友圈)
             *          <li>    header会显示部分或者显示全部
             *          <li>    根据header是否有layout_offset这个值作为参考
             *
             *  情景3
             *  <li>    上拉刷新结束之后，content是否增加高度
             *          <li>    如果增加，那么直接显示数据，而不是回弹
             *          <li>    如果不增加，那么要回弹
             */
            case IFrameDo.STATUS_NULL:
                if(content0 != null){
                    measureChild(content0,widthMeasureSpec,heightMeasureSpec);
                    childState = content0.getMeasuredState();
                    width = content0.getMeasuredWidth();
                    height = content0.getMeasuredHeight();

                    setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                            resolveSizeAndState(height, heightMeasureSpec,
                                    childState << MEASURED_HEIGHT_STATE_SHIFT));
                }
                return ;

            case IFrame.STATUS_HEADER:
                if(header0 != null && content0 != null){
                    measureChild(header0.getView(),widthMeasureSpec,heightMeasureSpec);
                    width = Math.max(width,header0.getView().getMeasuredWidth());
                    height += header0.getView().getMeasuredHeight();;
                    childState = combineMeasuredStates(childState,header0.getView().getMeasuredState());

                    measureChild(content0,widthMeasureSpec,heightMeasureSpec);
                    width = Math.max(width,content0.getMeasuredWidth());
                    height += content0.getMeasuredHeight();
                    childState = combineMeasuredStates(childState,content0.getMeasuredState());

                    setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                            resolveSizeAndState(height, heightMeasureSpec,
                                    childState << MEASURED_HEIGHT_STATE_SHIFT));
                }
                return ;

            case IFrame.STATUS_TAIL:
                if(tail0 != null && content0 != null){
                    measureChild(tail0.getView(),widthMeasureSpec,heightMeasureSpec);
                    width = Math.max(width,tail0.getView().getMeasuredWidth());
                    height += tail0.getView().getMeasuredHeight();
                    childState = combineMeasuredStates(childState,tail0.getView().getMeasuredState());

                    measureChild(content0,widthMeasureSpec,heightMeasureSpec);
                    width = Math.max(width,content0.getMeasuredWidth());
                    height += content0.getMeasuredHeight();
                    childState = combineMeasuredStates(childState,content0.getMeasuredState());

                    setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                            resolveSizeAndState(height, heightMeasureSpec,
                                    childState << MEASURED_HEIGHT_STATE_SHIFT));
                }
                return ;
        }

        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    public void freshChanged(int status) {
        this.status = status;
        requestLayout();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams0(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        LayoutParams0 params = new LayoutParams0(getContext(),attrs);
        /*
         *  container通过xml创建子View的时候，参数会通过这里和container产生关联
         *  <li>    属性可以子View持有
         *  <li>    属性也可以是container持有
         *
         *  <li>    子View在创建的时候回调用这个函数，只有当所有的子View创建完毕之后
         *          才会调用容器的onFinishInflate函数
         */
        return params;
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    @Override
    public boolean canFresh(int value) {
        return !content0.canScrollVertically(-value);
    }

    @Override
    public void complete() {
        executor.sem(IData.SEMAPHORE_COMPLETE);
    }

    @Override
    public IExecutor executor() {
        return executor;
    }

    @Override
    public IData data() {
        return data;
    }

    /*
     *  View Tree 结构(基于NOActionBar的主题结构) 内部如同一个双向链表
     *      >   com.android.internal.policy.impl.PhoneWindow$DecorView  (最底层的父容器，位于Window实现类PhoneWindow中的一个内部类 DecorView)
     *          >   LinearLayout
     *              >   ViewStub
     *
     *              >   FrameLayout
     *                  >   FitWindowsLinearLayout
     *                      >   ViewStubCompat
     *                      >   ContentFrameLayout
     *                          >   Activity 中的View
     *
     *
     *  不同的主题之间，内部的层次结构是不一样的，主要的出入在FrameLayout下面的ViewGroup
     *  这里的NOActionBar主题就是 FitWindowsLinearLayout
     *  DuckActionBar 主题就是 ActionbarOverLayLayout
     *
     *  Activity中的View在xml中的配置就是一个ViewGroup或者View(主题，通常是一个直接的View)
     *
     *      限定参数    (MeasureSpec)UNSPECIFIED, EXACTLY, AT_MOST
     *          >   UNSPECIFIED     未指定
     *          >   EXACTLY         精确的数值
     *          >   AT_MOST         限定一个最大的范围
     *
     *      View配置参数 (LayoutParam)
     *          >   MATCH_PARENT
     *          >   WRAP_CONTENT
     *          >   固定数值
     *
     *      View中配置的参数是一个相对于父容器的相对结果
     *      限定参数则是联合父容器和自身的参数，做出的一个预想结果
     *
     *      >   match_parent    匹配父容器
     *          >   自身的大小应该和父容器一样大
     *          >   自身的大小是和父容器直接关联的
     *          >   如果父容器指定为wrap_content 那么自身也将处于UNSPECIFIED
     *      >   wrap_content    包裹布局
     *          >   需要完全包裹自身的内容
     *          >   自身的大小是和子控件直接关联的 和父控件无关  自身会贴上(UNSPECIFIED标识)
     *          >   因为需要从下层的控件来决定自身控件的大小，那么这里需要至少测量两次 尽量避免wrap_content直接嵌套·
     *      >   固定数值         固定数值
     *
     *      match_parent wrap_content 本身的定义就是一个适应性的匹配，无法对布局本身的大小进行一个限定
     *      表达的一种相对而言的期待的结果
     *      最终会通过屏幕的大小和底层View的大小来决定最终的大小
     *
     *      MeasureSpec     测量参数，由期待的结果 + 具体的尺寸组成   (一个32位的int，前两位表示预期的结果，后30位标识具体的数值)
     *
     *          >   mode    期待的结果
     *          >   size    自身的尺寸
     */

    public static class LayoutParams0 extends MarginLayoutParams{
        //  标记 content header  tail
        public String type;
        //  如果view标记为ICue类型 需要使用该路径生成实力
        public String cueClassName;

        public int cue_offset;

        public LayoutParams0(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.IFrame_Layout, 0, 0);
            String s0 = ta.getString(R.styleable.IFrame_Layout_cue_type);
            if(s0 == null||!("header".equals(s0) || "tail".equals(s0) || "content".equals(s0))){
                s0 = "ksv";
            }
            type = s0;
            cueClassName = ta.getString(R.styleable.IFrame_Layout_cue_name);
            cue_offset = ta.getInt(R.styleable.IFrame_Layout_layout_offset,0);

            ta.recycle();
        }

        public LayoutParams0(int width, int height) {
            super(width, height);
        }

        public LayoutParams0(LayoutParams0 source) {
            super(source);
        }
    }
}