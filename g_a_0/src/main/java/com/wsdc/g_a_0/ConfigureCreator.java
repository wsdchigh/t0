package com.wsdc.g_a_0;

import com.alibaba.fastjson.JSON;
import com.wsdc.file.FileUtils;
import com.wsdc.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ConfigureCreator {
    int i = 0;
    public XInfo testInfo(){
        XInfo info = new XInfo();
        info.module_name = "user";
        info.name = "user-4.0.0.apk";
        info.version = "4.0.0";
        info.http_url = "http://www.baidu.com";
        info.local_url = "user-4.0.0.apk";
        info.plugins = new LinkedList<>();

        XInfo.XPlugin plugin = new XInfo.XPlugin();
        info.plugins.add(plugin);
        plugin.key = "/user/home";
        plugin.path = "com.wsdc.a.b";
        plugin.proxyPath = "com.wsdc.a.b";
        plugin.viewHolderPath = "com.wsdc.a.b";
        plugin.iDataPath = "com.wsdc.a.b";
        plugin.userParent = true;
        plugin.wrapKey = 1;

        return info;
    }

    public XInfoAll testInfoAll(){
        XInfoAll all = new XInfoAll();
        all.version = "4.0.0";
        all.versionCode = 400;
        return all;
    }

    public List<XInfo.WrapInfo> testWrapInfo(){
        List<XInfo.WrapInfo> infos = new LinkedList<>();

        XInfo.WrapInfo w1 = new XInfo.WrapInfo();
        w1.path = "com.wsdc.activity.A";
        w1.wrapKey = 100;
        w1.type = "activity";

        infos.add(w1);

        return infos;
    }

    /*
     *  wrap数据是由主APK生成的
     *  <li>    插件apk生成没有什么用处
     */
    public void wrapJson(List<XInfo.WrapInfo> infos){
        try{
            File root = new File("../configure_file");
            File file = new File(root,"wrap.json");
            IOUtils.write(JSON.toJSONString(infos).getBytes(),FileUtils.outputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void infoJson(XInfo info){
        try{
            File root = new File("../configure_file/json");
            File file = new File(root,info.module_name+"-"+info.version+(i++)+".json");
            IOUtils.write(JSON.toJSONString(info).getBytes(),FileUtils.outputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void infoAllJson(XInfoAll infoAll){
        File root = new File("../configure_file/json");
        File[] files = root.listFiles();
        File allJson = new File("../configure_file/apk.json");

        if(infoAll.infos == null){
            infoAll.infos = new LinkedList<>();
        }

        for (File file : files) {
            try {
                byte[] read = IOUtils.read(FileUtils.inputStream(file));
                XInfo info = JSON.parseObject(new String(read,"UTF-8"),XInfo.class);
                infoAll.infos.add(info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File wrapFile = new File("../configure_file/wrap.json");
        try {
            byte[] read = IOUtils.read(FileUtils.inputStream(wrapFile));
            List<XInfo.WrapInfo> infos = JSON.parseArray(new String(read,"UTF-8"),XInfo.WrapInfo.class);

            infoAll.wrapInfos = infos;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            IOUtils.write(JSON.toJSONString(infoAll).getBytes(),FileUtils.outputStream(allJson));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void clear(){
        File root = new File("../configure_file");
        if(root.exists()){
            FileUtils.delete(root);
        }

        File f1 = new File("../configure_file");
        File f2 = new File("../configure_file/json");
        File f3 = new File("../configure_file/apk");

        f1.mkdirs();
        f2.mkdirs();
        f3.mkdirs();
    }
}
