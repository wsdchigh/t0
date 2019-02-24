package com.wsdchigh.g_a_rely_static.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class UStatus {
    private static final UStatus instance = new UStatus();

    public static UStatus getInstance(){
        return instance;
    }

    /*
     *  获取状态栏的高度
     */
    public int getStatusHeight(Activity act){
        int resourcesID = act.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int dimension = act.getResources().getDimensionPixelSize(resourcesID);
        return dimension;
    }

    /*
     *  获取屏幕参数
     */

    /*
     *  获取版本信息
     */

    /*
     *  移除沉浸式的 头部偏移
     *  <li>    如果版本不是5.0及以上，取消第一个header(如果有需要)的paddingTop
     */
    public void removeStatusPaddingTop(View rootView,int offset){
        if(rootView instanceof ViewGroup){
            View view = ((ViewGroup) rootView).getChildAt(0);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            int top = view.getPaddingTop();
            view.setPadding(0,0,0,0);
            params.height = params.height-top-offset;
        }
    }
}
