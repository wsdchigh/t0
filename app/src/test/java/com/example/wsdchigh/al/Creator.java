package com.example.wsdchigh.al;

import com.example.wsdchigh.al.activity.GuideActivity;
import com.example.wsdchigh.al.activity.WrapActivity1;
import com.example.wsdchigh.al.activity.WrapActivity2;
import com.example.wsdchigh.al.activity.WrapActivity3;
import com.example.wsdchigh.al.activity.WrapActivity4;
import com.wsdc.g_a_0.ConfigureCreator;
import com.wsdc.g_a_0.XInfo;
import com.wsdc.g_a_0.XInfoAll;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class Creator {
    @Test
    public void create(){
        List<XInfo.WrapInfo> infos = new LinkedList<>();

        XInfo.WrapInfo info0 = new XInfo.WrapInfo();
        info0.wrapKey = 101;
        info0.type = "activity";
        info0.path = GuideActivity.class.getName();
        infos.add(info0);

        XInfo.WrapInfo info1 = new XInfo.WrapInfo();
        info1.wrapKey = 102;
        info1.type = "activity";
        info1.path = WrapActivity1.class.getName();
        infos.add(info1);


        XInfo.WrapInfo info2 = new XInfo.WrapInfo();
        info2.wrapKey = 103;
        info2.type = "activity";
        info2.path = WrapActivity2.class.getName();
        infos.add(info2);


        XInfo.WrapInfo info3 = new XInfo.WrapInfo();
        info3.wrapKey = 104;
        info3.type = "activity";
        info3.path = WrapActivity3.class.getName();
        infos.add(info3);

        XInfo.WrapInfo info4 = new XInfo.WrapInfo();
        info4.wrapKey = 105;
        info4.type = "activity";
        info4.path = WrapActivity4.class.getName();
        infos.add(info4);

        ConfigureCreator cc = new ConfigureCreator();

        cc.wrapJson(infos);

        XInfoAll infoAll = new XInfoAll();
        infoAll.version = "4.4.4";
        infoAll.versionCode = 444;
        //  创建apk.json
        cc.infoAllJson(infoAll);
    }
}
