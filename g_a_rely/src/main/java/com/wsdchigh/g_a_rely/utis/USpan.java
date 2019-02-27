package com.wsdchigh.g_a_rely.utis;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

/*
 *  关于富文本的介绍使用
 *  <li>    SpannableStringBuilder
 *  <li>    SpannableString
 *  <li>    如果StringBuilder和String之间的类似关系
 *          <li>    builder表达的是组建多个类型
 *          <li>    String则针对一个字符串进行处理
 *
 *  <li>    CharacterStyle  定义字符的类型span的基类
 *
 *  <li>    ForegroundColorSpan     前景颜色    对文本中的指定字符串进行字体颜色设置
 *  <li>    backgroundColorSpan     背景颜色    对文本的指定字符串的背景颜色进行设置
 *  <li>    RelativeSizeSpan        相对尺寸    对文本的指定字符串进行相对尺寸的设置
 *  <li>    StrikethroughSpan       删除线
 *  <li>    UnderlineSpan           下划线
 *  <li>    SuperscriptSpan         字符上浮
 *  <li>    SubscriptSpan           字符下沉
 *  <li>    StyleSpan               文本的样式，加粗?斜体?
 *  <li>    ImageSpan               添加图片    (图文并排)
 *          <li>    这是一个ReplaceSpan 是对区域的文本进行等量替换，所以需要实现占好位置
 *          <li>    drawable需要设置便捷
 *  <li>    ClickableSpan           支持点击
 *          <li>    语法高亮(需要设置TextView语法高亮的颜色)
 *          <li>    TextView需要设置Move
 */
public class USpan {
    private static final USpan instance = new USpan();

    public static USpan getInstance(){
        return instance;
    }

    public SpannableString colorSpan(String text,int start,int end,int color){
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcs = new ForegroundColorSpan(color);
        ss.setSpan(fcs,start,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /*
     *  图片代替文本
     *  <li>    需要提前占位
     *  <li>    drawable提前设置好区域 setBound
     */
    public SpannableString imageSpan(String text, int start, int end, Drawable drawable){
        SpannableString ss = new SpannableString(text);
        ImageSpan is = new ImageSpan(drawable);
        ss.setSpan(is,start,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
