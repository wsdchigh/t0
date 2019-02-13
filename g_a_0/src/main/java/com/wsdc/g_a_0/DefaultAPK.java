package com.wsdc.g_a_0;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class DefaultAPK implements APK {
    XInfo info;

    DexClassLoader classLoader;
    Resources resources;

    public DefaultAPK(XInfo info,Context context) {
        this.info = info;
        File optimizedDirectoryFile = context.getDir("dex", Context.MODE_PRIVATE);

        String filePath = new File(context.getFilesDir()+"start_wsdc/"+info.local_url).getAbsolutePath();
        classLoader = new DexClassLoader(filePath,
                optimizedDirectoryFile.getAbsolutePath(),null,getClass().getClassLoader());

        try{
            AssetManager assetManager = AssetManager.class.newInstance();   // 创建AssetManager实例
            Class cls = AssetManager.class;
            Method method = cls.getMethod("addAssetPath", String.class);
            method.invoke(assetManager, filePath);  // 反射设置资源加载路径
            resources = new Resources(assetManager,new DisplayMetrics(),new Configuration());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DexClassLoader classLoader() {
        return classLoader;
    }

    @Override
    public Resources resources() {
        return resources;
    }

    @Override
    public XInfo info() {
        return info;
    }
}
