package com.wsdc.g_a_0;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.wsdc.g_j_0.IContainer0;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/*
 *  考虑到有多个Resource
 *  <li>    不同的APK文件中有不同的资源
 *  <li>    但是他们会统一处理
 *  <li>    我们选择Activity返回这个  代理者  这里面会查询到第一个，然后返回
 *
 *  <li>    因为本地local中的资源非常少，所以不要讲local放在前面，减少异常的抛出
 *
 *
 *  <li>    针对任何一个APK文件创建一个 Resources
 *          <li>    不要对每一个Activity都去创建Resources
 *
 *  <li>    如果自身没有获取
 *          <li>    让local去取出来
 */
public class ResourceProxy1 extends Resources{
    private List<Resources> proxyList = new LinkedList<>();

    /*
     *  任何一个Activity都有一个Resource，底层的Resource是唯一的
     *  <li>    顶层封装底层，所以不同的Activity的Resources是不一样的
     *
     *  <li>    如果功能自身无法实现，或者功能应该local来执行
     *          <li>    获取屏幕数据等等
     *          <li>    我们使用local来表达数据
     */
    private Resources local;

    public ResourceProxy1(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }

    public ResourceProxy1(AssetManager assets) {
        /*
         *   针对任何一个APK文件，创建一个
         */
        this(assets,new DisplayMetrics(),new Configuration());

    }

    public Resources getLocal() {
        return local;
    }

    public void setLocal(Resources local) {
        this.local = local;
    }

    @Override
    public Drawable getDrawable(int id,Theme theme) throws NotFoundException {
        Drawable rtn = null;

        try{
            rtn = super.getDrawable(id,theme);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            if(Build.VERSION.SDK_INT >= 21){
                rtn = local.getDrawable(id,theme);
            }else{
                rtn = local.getDrawable(id);
            }
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("drawable not found ;id = "+id);
    }

    @Override
    public Drawable getDrawable(int id) throws NotFoundException {
        Drawable rtn = null;

        try{
            rtn = super.getDrawable(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getDrawable(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("drawable not found ;id = "+id);
    }

    @Override
    public CharSequence getText(int id) throws NotFoundException {
        CharSequence rtn = null;

        try{
            rtn = super.getText(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getText(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("text not found ;id = "+id);
    }

    @Override
    public Typeface getFont(int id) throws NotFoundException {
        Typeface rtn = null;

        try{
            rtn = super.getFont(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            if(Build.VERSION.SDK_INT >= 26){
                rtn = local.getFont(id);
                return rtn;
            }

        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("font not found ;id = "+id);
    }

    @Override
    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        CharSequence rtn = null;

        try{
            rtn = super.getQuantityText(id,quantity);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getQuantityText(id,quantity);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("text not found ;id = "+id);
    }


    @Override
    public String getString(int id) throws NotFoundException {
        String rtn = null;

        try{
            rtn = super.getString(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getString(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        String rtn = null;

        try{
            rtn = super.getString(id,formatArgs);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getString(id,formatArgs);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }



    @Override
    public String getQuantityString(int id, int quantity, Object... formatArgs) throws NotFoundException {
        String rtn = null;

        try{
            rtn = super.getQuantityString(id,quantity,formatArgs);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getQuantityString(id,quantity,formatArgs);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public String getQuantityString(int id, int quantity) throws NotFoundException {
        String rtn = null;

        try{
            rtn = super.getQuantityString(id,quantity);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getQuantityString(id,quantity);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);

    }

    @Override
    public CharSequence getText(int id, CharSequence def) {
        CharSequence rtn = null;

        try{
            rtn = super.getText(id,def);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getText(id,def);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public CharSequence[] getTextArray(int id) throws NotFoundException {
        CharSequence[] rtn = null;

        try{
            rtn = super.getTextArray(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getTextArray(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }


    @Override
    public String[] getStringArray(int id) throws NotFoundException {
        String[] rtn = null;

        try{
            rtn = super.getStringArray(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getStringArray(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }


    @Override
    public int[] getIntArray(int id) throws NotFoundException {
        int[] rtn = null;

        try{
            rtn = super.getIntArray(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getIntArray(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public TypedArray obtainTypedArray(int id) throws NotFoundException {
        TypedArray rtn = null;

        try{
            rtn = super.obtainTypedArray(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.obtainTypedArray(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public float getDimension(int id) throws NotFoundException {
        Float rtn = null;

        try{
            rtn = super.getDimension(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getDimension(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public int getDimensionPixelOffset(int id) throws NotFoundException {
        Integer rtn = null;

        try{
            rtn = super.getDimensionPixelOffset(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getDimensionPixelOffset(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public int getDimensionPixelSize(int id) throws NotFoundException {
        Integer rtn = null;

        try{
            rtn = super.getDimensionPixelSize(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getDimensionPixelSize(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public float getFraction(int id, int base, int pbase) {
        Float rtn = null;

        try{
            rtn = super.getFraction(id,base,pbase);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getFraction(id,base,pbase);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }


    @Override
    public Drawable getDrawableForDensity(int id, int density) throws NotFoundException {
        Drawable rtn = null;

        try{
            rtn = super.getDrawableForDensity(id,density);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getDrawableForDensity(id,density);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }


    @Override
    public Drawable getDrawableForDensity(int id, int density, Theme theme) {
        Drawable rtn = null;

        try{
            rtn = super.getDrawableForDensity(id,density,theme);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            if (Build.VERSION.SDK_INT >= 21){
                rtn = local.getDrawableForDensity(id,density,theme);
            }else{
                rtn = local.getDrawableForDensity(id,density);
            }

            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public Movie getMovie(int id) throws NotFoundException {
        Movie rtn = null;

        try{
            rtn = super.getMovie(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getMovie(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public int getColor(int id) throws NotFoundException {
        Integer rtn = null;

        try{
            rtn = super.getColor(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getColor(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public int getColor(int id, Theme theme) throws NotFoundException {
        Integer rtn = null;
        try{
            rtn = super.getColor(id,theme);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            if(Build.VERSION.SDK_INT >= 23){
                rtn = local.getColor(id,theme);
            }else{
                rtn = local.getColor(id);
            }

            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }


    @Override
    public ColorStateList getColorStateList(int id) throws NotFoundException {
        ColorStateList rtn = null;

        try{
            rtn = super.getColorStateList(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getColorStateList(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }


    @Override
    public ColorStateList getColorStateList(int id, Theme theme) throws NotFoundException {
        ColorStateList rtn = null;

        try{
            rtn = super.getColorStateList(id,theme);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            if(Build.VERSION.SDK_INT >= 23){
                rtn = local.getColorStateList(id,theme);
            }else{
                rtn = local.getColorStateList(id);
            }

            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public boolean getBoolean(int id) throws NotFoundException {
        Boolean rtn = null;

        try{
            rtn = super.getBoolean(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getBoolean(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public int getInteger(int id) throws NotFoundException {
        Integer rtn = null;

        try{
            rtn = super.getInteger(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getInteger(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public XmlResourceParser getLayout(int id) throws NotFoundException {
        XmlResourceParser rtn = null;

        try{
            rtn = super.getLayout(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getLayout(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }


    @Override
    public XmlResourceParser getAnimation(int id) throws NotFoundException {
        XmlResourceParser rtn = null;

        try{
            rtn = super.getAnimation(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getAnimation(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }


    @Override
    public XmlResourceParser getXml(int id) throws NotFoundException {
        XmlResourceParser rtn = null;

        try{
            rtn = super.getXml(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getXml(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public InputStream openRawResource(int id) throws NotFoundException {
        InputStream rtn = null;

        try{
            rtn = super.openRawResource(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.openRawResource(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }


    @Override
    public InputStream openRawResource(int id, TypedValue value) throws NotFoundException {
        InputStream rtn = null;
        try{
            rtn = super.openRawResource(id,value);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.openRawResource(id,value);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);

    }

    @Override
    public AssetFileDescriptor openRawResourceFd(int id) throws NotFoundException {
        AssetFileDescriptor rtn = null;
        try{
            rtn = super.openRawResourceFd(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.openRawResourceFd(id);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public void getValue(int id, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        try{
            super.getValue(id,outValue,resolveRefs);
            return;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            local.getValue(id,outValue,resolveRefs);
            return;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public void getValueForDensity(int id, int density, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        try{
            super.getValueForDensity(id,density,outValue,resolveRefs);
            return;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            local.getValueForDensity(id,density,outValue,resolveRefs);
            return;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = "+id);
    }

    @Override
    public void getValue(String name, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        try{
            super.getValue(name,outValue,resolveRefs);
            return;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            local.getValue(name,outValue,resolveRefs);
            return;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = ");
    }

    //  TODO    这里需要谨慎考虑，到底使用自己还是 local
    @Override
    public TypedArray obtainAttributes(AttributeSet set, int[] attrs) {
        //return local.obtainAttributes(set,attrs);
        return local.obtainAttributes(set,attrs);
    }

    @Override
    public void updateConfiguration(Configuration config, DisplayMetrics metrics) {
        local.updateConfiguration(config,metrics);
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        return local.getDisplayMetrics();
    }

    @Override
    public Configuration getConfiguration() {
        return local.getConfiguration();
    }

    @Override
    public int getIdentifier(String name, String defType, String defPackage) {
        return local.getIdentifier(name,defType,defPackage);
    }

    @Override
    public String getResourceName(int resid) throws NotFoundException {
        String rtn = null;
        try{
            rtn = super.getResourceName(resid);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getResourceName(resid);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = ");
    }

    @Override
    public String getResourcePackageName(int resid) throws NotFoundException {
        String rtn = null;
        try{
            rtn = super.getResourcePackageName(resid);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getResourcePackageName(resid);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = ");
    }

    @Override
    public String getResourceTypeName(int resid) throws NotFoundException {
        String rtn = null;
        try{
            rtn = super.getResourceTypeName(resid);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getResourceTypeName(resid);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = ");
    }

    @Override
    public String getResourceEntryName(int resid) throws NotFoundException {
        String rtn = null;
        try{
            rtn = super.getResourceEntryName(resid);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        try{
            rtn = local.getResourceEntryName(resid);
            return rtn;
        }catch (NotFoundException e){
            e.printStackTrace();
        }

        throw new NotFoundException("string not found ;id = ");
    }

    @Override
    public void parseBundleExtras(XmlResourceParser parser, Bundle outBundle) throws IOException, XmlPullParserException {
        local.parseBundleExtras(parser,outBundle);
    }

    @Override
    public void parseBundleExtra(String tagName, AttributeSet attrs, Bundle outBundle) throws XmlPullParserException {
        local.parseBundleExtra(tagName, attrs, outBundle);
    }
}
