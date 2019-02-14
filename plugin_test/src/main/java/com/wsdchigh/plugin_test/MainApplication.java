package com.wsdchigh.plugin_test;

import android.app.Application;

import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.router.IRouter;

public class MainApplication extends Application {
    Starter starter;
    IRouter router;

    @Override
    public void onCreate() {
        super.onCreate();
        Starter.install(this);
        starter = Starter.getInstance();
        router = starter.getRouter();
        router.go("/test/t0",1);
    }
}
