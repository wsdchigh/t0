package com.wsdc.g_a_0.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 *  <li>    标记类
 *  <li>    只需要在class中生效，不加载到运行时
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface APlugin {
    String key();
    //  path 可以根据反射获得
    //String path() default "null";
    PluginSign sign();

    //  IData需要使用这个标记  其他不需要
    boolean userParent() default false;

    //  activity+fragment联合使用 需要这个ID    在Proxy表明即可，其他位置不去读取
    int fragmentContainerID() default -1;

    //  插件的服务ID     在proxy表明即可      -1 只是让其他位置不写而已
    int wrapKey() default -1;
}
