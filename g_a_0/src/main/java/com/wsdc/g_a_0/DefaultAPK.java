package com.wsdc.g_a_0;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.PathClassLoader;

public class DefaultAPK implements APK {
    XInfo info;

    BaseDexClassLoader classLoader;
    Resources resources;

    public DefaultAPK(XInfo info,Context context,ClassLoader parent) {
        this.info = info;

        //  标识为自身APK携带的模块
        if(info.local){
            classLoader = (BaseDexClassLoader) context.getClassLoader();
            resources = context.getResources();
            return;
        }
        File optimizedDirectoryFile = context.getDir("dex", Context.MODE_PRIVATE);

        File file0 = new File(context.getFilesDir() + "/start_wsdc/apks/" + info.local_url);
        Log.d("wsdc", "file is exists = "+file0.exists());
        Log.d("wsdc", "file path "+file0.getAbsolutePath());
        String filePath = file0.getAbsolutePath();

        /*
        classLoader = new DexClassLoader(filePath,
                optimizedDirectoryFile.getAbsolutePath(),null,parent);
                */



        classLoader = new PathClassLoader(filePath,parent);

        try {
            Class<?> clz = classLoader.loadClass("com.wsdc.plugin_test.plugin.global.GlobalProxy");
            Log.d("wsdc", "class class class" );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("wsdc", "failure failure failure");
        }

        try{
            AssetManager assetManager = AssetManager.class.newInstance();   // 创建AssetManager实例
            Class cls = AssetManager.class;
            Method method = cls.getMethod("addAssetPath", String.class);
            method.invoke(assetManager, filePath);  // 反射设置资源加载路径
            //resources = new Resources(assetManager,new DisplayMetrics(),new Configuration());
            resources = new ResourceProxy1(assetManager);
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
}
