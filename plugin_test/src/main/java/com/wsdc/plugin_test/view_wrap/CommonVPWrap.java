package com.wsdc.plugin_test.view_wrap;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CommonVPWrap implements View.OnTouchListener {
    private ViewPager vp;
    private Handler handler;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            int index = vp.getCurrentItem();
            vp.setCurrentItem(index + 1);
            handler.postDelayed(r,3000);
        }
    };

    public CommonVPWrap(ViewPager vp, Handler handler) {
        this.vp = vp;
        this.handler = handler;

        //vp.setOnTouchListener(this);
        handler.postDelayed(r,5000);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(r);
                Log.d("wsdc1", "移除");
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handler.postDelayed(r,5000);
                Log.d("wsdc1", "添加");
                break;
        }
        return false;
    }

    public void close(){
        handler.removeCallbacks(r);
        Log.d("wsdc1", "移除  移除  移除");
    }
}
