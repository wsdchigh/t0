package com.wsdchigh.g_a_rely.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.wsdchigh.g_a_rely.R;

/*
 *  配合ViewPager的显示组件
 *  <li>    实现vp页面滑动监听，实现联动
 *  <li>    支持外部设置 drawable 来改变样式
 */
public class XIndicate extends LinearLayout implements ViewPager.OnPageChangeListener {
    int count = 0;
    int now = 0;

    Drawable checked,unchecked;

    public XIndicate(Context context) {
        this(context,null);
    }

    public XIndicate(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XIndicate(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        checked = context.getResources().getDrawable(R.drawable.indicate_default_checked);
        unchecked = context.getResources().getDrawable(R.drawable.indicate_default_unchecked);
    }

    /*
     *  如果想要自定义drawable，调用这个函数
     *  <li>    这个函数需要在setCount之前调用
     */
    public void setDrawable0(Drawable checked,Drawable unchecked){
        this.checked = checked;
        this.unchecked = unchecked;
    }

    public void setCount(int count){
        this.count = count;
        removeAllViews();

        for (int i = 0; i < count; i++) {
            View v = new View(getContext());
            if(Build.VERSION.SDK_INT < 16){
                v.setBackgroundDrawable(unchecked);
            }else{
                v.setBackground(unchecked);
            }
            LinearLayout.MarginLayoutParams params = new LayoutParams(14,14);
            params.setMargins(4,4,4,4);
            v.setLayoutParams(params);

            addView(v);
        }
        invalidate();
    }

    public void setNow(int now){
        View v = getChildAt(this.now);
        if(Build.VERSION.SDK_INT < 16){
            v.setBackgroundDrawable(unchecked);
        }else{
            v.setBackground(unchecked);
        }

        View v1 = getChildAt(now);
        if(Build.VERSION.SDK_INT < 16){
            v1.setBackgroundDrawable(checked);
        }else{
            v1.setBackground(checked);
        }

        v.invalidate();
        v1.invalidate();
        this.now = now;
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        setNow(i%count);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
