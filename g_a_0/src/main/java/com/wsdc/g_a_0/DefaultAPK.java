package com.wsdc.g_a_0;

import android.content.res.Resources;

import dalvik.system.DexClassLoader;

public class DefaultAPK implements APK {
    Configure0.Apk0 apk0;

    DexClassLoader classLoader;
    Resources resources;

    public DefaultAPK(Configure0.Apk0 apk0) {
        this.apk0 = apk0;
    }

    @Override
    public DexClassLoader classLoader() {
        return null;
    }

    @Override
    public Resources resources() {
        return null;
    }
}
