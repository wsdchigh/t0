package com.wsdc.g_a_0;

import java.util.List;

/*
 *  整合所有的apk信息
 *  <li>    如果版本比较多，那么会存储最近3个版本的信息
 */
public class XInfoAll {
    /*
     *  总的版本
     *  <li>    如果本地的总版本和服务器的总版本不一样
     *          <li>    此时需要比对对应apk的版本
     *          <li>    相同，则不处理
     *          <li>    不同，则下载
     *  <li>    只有总版本变了才会去检查apk版本
     *
     */
    public String version;
    public int versionCode;
    public List<XInfo> infos;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public List<XInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<XInfo> infos) {
        this.infos = infos;
    }
}
