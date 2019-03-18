package com.wsdc.g_a_0;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.PathClassLoader;

/*
 *  支持同步/异步
 */
public class DefaultMultiApk implements APK {
    XInfo info;
    BaseDexClassLoader classLoader;
    ClassLoader parent;
    Resources resources;
    boolean loading = false;
    Context context;

    public DefaultMultiApk(XInfo info, Context context, ClassLoader parent) {
        this.info = info;
        this.context = context;
        this.parent = parent;
        /*
         *  表示为本地apk
         *  <li>    测试单个插件的时候会使用
         */
        if(info.local){
            classLoader = (BaseDexClassLoader) context.getClassLoader();
            resources = context.getResources();
            return;
        }

        if(info.sync){
            try {
                loading();
                loading = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //  starter自动添加到异步池中
    }

    @Override
    public BaseDexClassLoader classLoader() {
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

    @Override
    public boolean hasLoading() {
        return loading;
    }

    @Override
    public void loading() throws Exception {
        File file0 = new File(context.getFilesDir()+ "/start_wsdc/apks/" + info.local_url);
        String filePath = file0.getAbsolutePath();
        classLoader = new PathClassLoader(filePath,parent);

        AssetManager assetManager = AssetManager.class.newInstance();   // 创建AssetManager实例
        Class cls = AssetManager.class;
        Method method = cls.getMethod("addAssetPath", String.class);
        method.invoke(assetManager, filePath);  // 反射设置资源加载路径
        //resources = new Resources(assetManager,new DisplayMetrics(),new Configuration());
        resources = new ResourceProxy1(assetManager);
    }
}
