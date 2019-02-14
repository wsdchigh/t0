package com.wsdc.g_a_0;

import android.content.Context;
import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.wsdc.file.FileUtils;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouter;
import com.wsdc.g_a_0.router.inner.DefaultIRouterImpl;
import com.wsdc.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 *  启动者
 *  <li>    通过一系列的配置文件启动，配置文件存放在 /file/starter_wsdc目录下面
 *  <li>    需要在assets中存放一份最简单的配置文件
 *          <li>    如果目录不存在，会在assets中复制一份过来
 *
 *  <li>    配置启动业的相关数据
 *  <li>    配置首页的相关数据
 *
 *  <li>    配置插件的相关信息
 *          <li>    插件的key
 *          <li>    插件的路径
 *          <li>    proxy的路径
 *          <li>    viewHolder的路径
 *          <li>    IData的路径
 *          <li>    IData是否使用parent的IData对象 (不独立创建)
 *          <li>    apk文件的位置
 *          <li>    apk的版本信息
 *                  <li>    需要和服务端进行比对，不同则需要下载
 *
 *          <li>    classLoader的路径
 *          <li>    服务的类型   (Activity?Fragment)
 *                  <li>    不同的插件，需要的Activity可能有一点差别
 *                  <li>    这里使用一个Integer去保存
 *
 *  <li>    Activity配置信息
 *          <li>    内置的Activity (因为必须在注册文件中注册)
 *          <li>    activity的路径
 *          <li>    对应的key  (不同的activity的key可以相同，相同视为一组)
 *
 *  <li>    Service配置信息 (必须在清单文件中注册注册)
 *          <li>    配置一些服务在里面
 *
 *  <li>    广播的注册
 *          <li>    不是必须    广播可以动态注册
 *
 *  <li>    配置一个初始的自动路由
 *          <li>    启动页 ->  那个插件
 */
public class Starter{
    /*
     *  服务端下发的apk配置信息
     *  <li>    版本
     *  <li>    里面保留最近3个版本的的apk信息
     *          <li>    可以根据里面的信息，删除失效的apk包
     *
     *  <li>    里面保留最近3个版本的配置信息
     */
    private String apk_json = "apk.json";

    /*
     *  存放apk的位置
     */
    private String apk_files = "apks";

    private Context context;
    private File rootFile;
    private File apkJson;
    private File apkFiles;

    private XInfoAll infoAll;
    private IRouter router;

    /*
     *  全局插件
     *  <li>    这个插件不参与路由，用于全局的数据投递
     */
    private IPlugin globalPlugin;

    public Starter(Context context) {
        this.context = context;

        if(!checkConfig()){
            copyConfig();
        }
        loadingConfig();
        asyncToggle();
    }

    /*
     *  启动的位置
     *  <li>    检查配置
     *          <li>    如果配置不存在，那么去assets中加载数据
     *          <li>    复制一份到本地中进行存储
     *
     *  <li>    解析配置
     *
     *
     *  <li>    创建路由和插件
     *
     *
     *  <li>    服务器异步访问，同步数据
     *          <li>    比对版本，不同则下载
     *          <li>    删除过时数据，避免占用过多内存
     */
    public static void install(Context context){
        instance = new Starter(context);
    }

    private static Starter instance;

    public static Starter getInstance(){
        return instance;
    }

    /*
     *  如果没有下面的文件夹
     *  <li>    第一次使用
     *  <li>    用户清空了缓存
     *
     *  <li>    此时需要在assets下复制内容
     */
    private boolean checkConfig(){
        rootFile = new File(context.getFilesDir(),"start_wsdc");
        if(!rootFile.exists()){
            return false;
        }

        apkJson = new File(rootFile,apk_json);
        apkFiles = new File(rootFile,apk_files);
        return true;
    }

    /*
     *  apk.json
     *  apks    下的所有apk文件
     *
     *  <li>    不管怎么样 apk.json文件是一定要有的
     *  <li>    如果没有存放插件 那么apks下面是可以没有有效的文件的
     */
    private void copyConfig(){
        try{
            AssetManager assets = context.getAssets();
            InputStream is = assets.open(apk_json);
            if(!rootFile.exists()){
                rootFile.mkdirs();
            }
            apkJson = new File(rootFile,apk_json);
            apkFiles = new File(rootFile,apk_files);
            apkFiles.mkdirs();

            OutputStream os = FileUtils.outputStream(apkJson);
            IOUtils.write(is,os);

            String[] apks = assets.list("apks");
            for (String fileName : apks) {
                final String name = fileName.replace("apks/","");
                final File tmpFile = new File(apkFiles,name);

                IOUtils.write(assets.open(fileName),FileUtils.outputStream(tmpFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     *  加载配置信息
     *  <li>    加载apk.json获取一个  XInfoAll 实例
     */
    private void loadingConfig(){
        try {
            byte[] data = IOUtils.read(FileUtils.inputStream(apkJson));
            infoAll = JSON.parseObject(new String(data,"utf-8"),XInfoAll.class);
            router = new DefaultIRouterImpl(infoAll,context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void asyncToggle(){

    }

    /*
     *  清理数据
     */
    private void invalid(){

    }

    /*
     *  下载数据
     */
    private void download(){

    }

    /*
     *  重启APP
     */
    private void restart(){

    }


    public IRouter getRouter(){
        return router;
    }

    public IPlugin globalPlugin(){
        return globalPlugin;
    }

}
