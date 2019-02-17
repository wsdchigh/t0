package com.wsdc.g_a_0.annotation;

import com.wsdc.file.FileUtils;
import com.wsdc.g_a_0.RouterUtil;
import com.wsdc.g_a_0.XInfo;
import com.wsdc.g_a_0.XInfoAll;
import com.wsdc.g_a_0.plugin.inner.DefaultMainIData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
 *  注解
 *  <li>    描述插件的相关信息
 *  <li>    动态的收集插件
 *
 *  <li>    AProxy          标记Proxy
 *  <li>    AViewHolder     标记ViewHolder
 *  <li>    AData           标记data  data可以使用系统默认实现的，如果没有，会使用系统自带的
 *  <li>    AWrap           标记容器 Activity?Fragment?Dialog
 *  <li>    AApk            标记APK的相关信息
 *                          <li>    整个APK文件中，只需要任何一个类，带有该标记就行
 */
public class PluginProcessor {
    /*
     *  注解解析器
     *  <li>    该注解解析器，连同注解，均只是动态的生成路由表
     *          <li>    先生成动态路由表
     *          <li>    到时候，系统根据路由表，去加载信息
     *
     *  <li>    扫描整个class目录,收集所有带有上述注解的类
     *
     *  <li>    本质是生成 XInfo XInfoAll 的信息
     *          <li>    动态生成，减少工作量
     *
     *  <li>    不使用java自带的解析器，引入比较复杂，自定义一个扫描解析器
     *
     *  <li>    src  class源码地址      dest 生成文件的存放地址    suffix 过滤器(类地址必须携带该前缀)
     *          <li>    src可以取  /build/intermediates/javac/debug/compileDebugJavaWithJavac/class
     *                  <li>    不同的gradle版本可能不一样
     *                  <li>    该任务需要依赖编译任务 (需要先编译完成)
     *                  <li>    因为需要编译为release版本的apk文件，所以 需要完成一次apk的编译
     *
     *          <li>    dest    存放在父工程下  configure_file 目录里面
     *                  <li>    apk 存放apk文件
     *                  <li>    json存放针对每一个apk文件的信息
     *                  <li>    apk.json    生成最后统一的json文件
     *                  <li>    wrap.json   存放wrap信息的json文件
     *                  <li>    文件夹一定要先创建   (如果没有文件夹)
     *
     *          <li>    suffix  过滤报名
     *                  <li>    我们可能会引入一些其他的依赖
     *                  <li>    我们需要限定最小的位置
     */
    public void process(File src,File dest,String suffix){
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{src.toURI().toURL()},this.getClass().getClassLoader());
            List<File>[] array = new List[2];
            array[0] = new LinkedList<>();
            array[1] = new LinkedList<>();

            FileUtils.listFiles(array,src);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void process0() throws Exception {
        File src = new File("E:\\al\\plugin_test\\build\\intermediates\\javac\\debug\\compileDebugJavaWithJavac\\classes");
        File dest = new File("E:\\al\\configure_file_1");
        String suffix = new String("com.wsdc");
        List<File>[] array = new List[2];
        array[0] = new LinkedList<>();
        array[1] = new LinkedList<>();

        URLClassLoader classLoader = new URLClassLoader(new URL[]{src.toURI().toURL()},this.getClass().getClassLoader());
        FileUtils.listFiles(array,src);

        List<String> classNames = new LinkedList<>();
        String base = src.getAbsolutePath()+"\\";

        for (File file : array[1]) {
            String path0 = file.getAbsolutePath();
            String path1 = path0.replace(base, "");

            if(path1.endsWith(".class")){
                String path2 = path1.replace(".class","").replace("\\",".");
                if(path2.startsWith(suffix)){
                    classNames.add(path2);
                }
            }
        }



        for (String className : classNames) {
            Class<?> clz = classLoader.loadClass(className);
            System.out.println(clz.getName());
        }
    }

    private static final class JavaBean{
        public String packageName;
        public String className;
    }

    public static void main(String[] args){
        String s = args[0];

        File file = new File(s);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] data = new byte[64];
            int len = 0;

            while((len = fis.read(data)) != -1){
                bos.write(data,0,len);
            }

            s = new String(bos.toByteArray());
            fis.close();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path1 = args[1];

        Bean0 bean = null;

        try{
            String classpath = path1;
            File classFile = new File(classpath);
            URLClassLoader loader = new URLClassLoader(new URL[]{classFile.toURI().toURL()},ClassLoader.getSystemClassLoader());
            Class clz = loader.loadClass("com.alibaba.fastjson.JSON");
            Method method = clz.getDeclaredMethod("parseObject",String.class,Class.class);
            Method toJson = clz.getDeclaredMethod("toJSONString",Object.class);
            method.setAccessible(true);
            bean = (Bean0) method.invoke(null,s,Bean0.class);


            URL[] urlArray = new URL[bean.urls.size()];
            int index = 0;
            for (String url : bean.urls) {
                urlArray[index++] = new File(url).toURI().toURL();
            }

            URLClassLoader cl1 = new URLClassLoader(urlArray,PluginProcessor.class.getClassLoader());

            List<Class> apkList = new LinkedList<>();
            List<Class> proxyList = new LinkedList<>();
            List<Class> viewHolderList = new LinkedList<>();
            List<Class> dataList = new LinkedList<>();
            List<Class> wrapList = new LinkedList<>();


            for (String className : bean.classNames) {
                Class<?> c1 = cl1.loadClass(className);

                Annotation[] annotations = c1.getAnnotations();
                Annotation an = null;

                if((an = contain(annotations,AApk.class)) != null){
                    apkList.add(c1);
                }else if((an = contain(annotations,APlugin.class)) != null){
                    APlugin plugin = (APlugin) an;
                    if(plugin.sign() == PluginSign.PROXY){
                        proxyList.add(c1);
                    }else if(plugin.sign() == PluginSign.VIEW_HOLDER){
                        viewHolderList.add(c1);
                    }else{
                        dataList.add(c1);
                    }

                }else if((an = contain(annotations,AWrap.class)) != null){
                    AWrap aWrap = (AWrap) an;
                    wrapList.add(c1);
                }
            }

            List<XInfo> infoList = new LinkedList<>();
            Map<String,XInfo> infoMap = new TreeMap<>();
            XInfoAll infoAll = new XInfoAll();
            infoAll.infos = infoList;
            infoAll.wrapInfos = new LinkedList<>();
            XInfo mainInfo = null;

            for (Class clz4 : apkList) {
                AApk aApk = (AApk) clz4.getAnnotation(AApk.class);
                XInfo info0 = new XInfo();
                info0.module_name = aApk.module_name();
                info0.name = aApk.name();
                info0.version = aApk.version();
                info0.http_url = aApk.http_url();
                info0.local_url = aApk.local_url();
                info0.local = aApk.local();
                if(info0.local){
                    mainInfo = info0;
                }

                info0.plugins = new LinkedList<>();
                infoList.add(info0);

                infoMap.put(info0.module_name,info0);
            }

            infoAll.version = mainInfo.version;

            for (Class clz5 : proxyList) {
                //  插件里面的3个元素 key值是一样的
                APlugin proxy = (APlugin) clz5.getAnnotation(APlugin.class);
                String route = proxy.key();
                RouterUtil.RouterBean routerBean = RouterUtil.parse(route);

                XInfo info5 = infoMap.get(routerBean.module_name);

                if(info5 == null){
                    throw new RuntimeException("模块未注册  "+routerBean.module_name);
                }

                XInfo.XPlugin plugin5 = new XInfo.XPlugin();
                plugin5.key = proxy.key();
                plugin5.path = clz5.getName();
                plugin5.userParent = proxy.userParent();
                plugin5.wrapKey = proxy.wrapKey();
                plugin5.fragmentContainerID = proxy.fragmentContainerID();

                info5.plugins.add(plugin5);
            }

            for (Class clz6 : viewHolderList) {
                APlugin vh = (APlugin) clz6.getAnnotation(APlugin.class);
                String route = vh.key();
                RouterUtil.RouterBean routerBean = RouterUtil.parse(route);

                XInfo info6 = infoMap.get(routerBean.module_name);

                if(info6 == null){
                    throw new RuntimeException("模块未注册  "+routerBean.module_name);
                }

                XInfo.XPlugin plugin6 = null;

                for (XInfo.XPlugin plugin : info6.plugins) {
                    if(plugin.key.equals(vh.key())){
                        plugin6 = plugin;
                        break;
                    }
                }

                plugin6.viewHolderPath = clz.getName();
            }

            for (Class clz7 : dataList) {
                APlugin data = (APlugin) clz7.getAnnotation(APlugin.class);
                String route = data.key();
                RouterUtil.RouterBean routerBean = RouterUtil.parse(route);

                XInfo info7 = infoMap.get(routerBean.module_name);

                if(info7 == null){
                    throw new RuntimeException("模块未注册  "+routerBean.module_name);
                }

                XInfo.XPlugin plugin7 = null;

                for (XInfo.XPlugin plugin : info7.plugins) {
                    if(plugin.key.equals(data.key())){
                        plugin7 = plugin;
                        break;
                    }
                }

                plugin7.iDataPath = clz.getName();
            }

            //  因为是否需要使用默认的 data   我们需要检测
            //  检测 没有data的 设置默认的data
            //  没有vh的抛异常

            for (XInfo info : infoAll.infos) {
                for (XInfo.XPlugin plugin : info.plugins) {
                    if(plugin.viewHolderPath == null){
                        throw new RuntimeException("vh == null  "+plugin.key);
                    }

                    if(plugin.iDataPath == null){
                        plugin.iDataPath = DefaultMainIData.class.getName();
                    }
                }
            }

            //  检测wrap
            for (Class clz8 : wrapList) {
                if(clz8.getName().startsWith(bean.mainModule)){
                    AWrap aWrap = (AWrap) clz8.getAnnotation(AWrap.class);
                    XInfo.WrapInfo wi = new XInfo.WrapInfo();
                    wi.path = clz8.getName();
                    wi.type = aWrap.type().getName();
                    wi.wrapKey = aWrap.wrapKey();

                    infoAll.wrapInfos.add(wi);
                }
            }


            //  生成JSON
            String content = (String) toJson.invoke(null, infoAll);
            byte[] bytes = content.getBytes();

            File outFile = new File(file.getParentFile(),"all.json");
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(bytes,0,bytes.length);

            fos.flush();
            fos.close();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class Bean0{
        List<String> urls;
        List<String> classNames;
        /*
         *  只有该模块的wrap参与json生成
         *  <li>    我们通过报名识别    com.wsdc.app?com.wsdc.test
         *          <li>    通过开头决定模块
         */
        String mainModule;
        String destPath;

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

        public List<String> getClassNames() {
            return classNames;
        }

        public void setClassNames(List<String> classNames) {
            this.classNames = classNames;
        }

        public String getMainModule() {
            return mainModule;
        }

        public void setMainModule(String mainModule) {
            this.mainModule = mainModule;
        }

        public String getDestPath() {
            return destPath;
        }

        public void setDestPath(String destPath) {
            this.destPath = destPath;
        }
    }

    private static Annotation contain(Annotation[] annotations,Class clz){
        for (Annotation annotation : annotations) {
            if(annotation.getClass().getName().equals(clz.getName())){
                return annotation;
            }
        }

        return null;
    }
}
