package com.wsdc.g_a_0.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AApk {
    String module_name();
    String name();
    String version();
    String http_url();

    //  标记是否为主APK
    boolean local() default false;

    //  本地url是可以根据module_name + version 的名字动态生成
    String local_url();
}
