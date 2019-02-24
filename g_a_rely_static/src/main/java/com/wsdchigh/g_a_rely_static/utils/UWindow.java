package com.wsdchigh.g_a_rely_static.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/*
 *  关于window的一些指导意见 (UI层，沉浸式)
 *  <li>    window
 *          <li>    主要是标记
 *          <li>    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);      移除这个标记，否则无法修改状态栏
 *          <li>    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  状态栏进入可以绘制模式
 *          <li>    window.setStatusBarColor(color);                                            状态栏设置背景颜色
 *  <li>    window.getDecorView (DecorView)
 *          <li>    主要是实现
 *          <li>    setSystemUiVisibility(int)  只能设置一次，后面的设置会移除掉之前的设置
 *                  如果需要一次性设置多种数据，使用  | 语法,链接所有的状态  (属性通常在View中声明),专门为这个函数声明了多个属性
 *
 *                  <li>    View.
 */
public class UWindow {
    private static final UWindow instance = new UWindow();

    public static UWindow getInstance(){
        return instance;
    }


    public void fullScreen(Activity act,int color) {
        if(Build.VERSION.SDK_INT >= 23){
            Window window = act.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);

            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        }
    }

    public void setStatusTextColor(Activity act,boolean isBlack){
        Window window = act.getWindow();

        if(isBlack){
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }else{
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if(Build.VERSION.SDK_INT >= 21){
            window.setNavigationBarColor(Color.BLACK);
        }

    }

    public void hideNavigation(Activity act) {
        Window window = act.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void setWindowStatusBarColor(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);

                //底部导航栏
                window.setNavigationBarColor(Color.BLACK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                window.setNavigationBarColor(dialog.getContext().getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImgTransparent(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  //不隐藏和透明虚拟导航栏  因为会遮盖底部的布局
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE//保持布局状态
                    |   View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);//不隐藏和透明虚拟导航栏  因为会遮盖底部的布局

        }
    }
}