package com.wsdchigh.g_a_rely.views;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wsdc.g_j_0.IPeriod0;

/*
 *  扩展ViewPager
 *  <li>    允许定时切换
 *  <li>    如果vp上面有事件发生，清除定时滑动，直到up，cancel事件的再次发出
 */
public class XViewPager extends ViewPager implements IPeriod0<Handler> {
    private Handler handler;
    private boolean exit = false;
    private OnTouchListener touch = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    send0();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    cancel0();
                    break;
            }
            return false;
        }
    };

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            setCurrentItem(currentItem+1);
            if(!exit){
                send0();
            }
        }
    };


    public XViewPager(Context context) {
        super(context);
    }

    public XViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean rtn = super.dispatchTouchEvent(ev);
        if(handler != null){
            touch.onTouch(this,ev);
        }
        return rtn;
    }

    @Override
    public void init(Handler handler) {
        if(this.handler == null){
            this.handler = handler;
            send0();
        }
    }

    @Override
    public void exit(Handler handler) {
        cancel0();
    }

    private void send0(){
        if(handler != null){
            handler.postDelayed(r,5000);
        }
    }

    private void cancel0(){
        if(handler != null){
            handler.removeCallbacks(r);
            exit = true;
        }
    }
}
