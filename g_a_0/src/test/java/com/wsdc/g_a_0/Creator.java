package com.wsdc.g_a_0;

import com.alibaba.fastjson.JSON;
import com.wsdc.file.FileUtils;
import com.wsdc.io.IOUtils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Creator {
    /*
     *  创建启动json
     *  <li>    在gradle中去创建的话
     */
    @Test
    public void configureCreator(){
        ConfigureCreator cc = new ConfigureCreator();

        if(true){
            cc.clear();

            return ;
        }

        for (int i = 0; i < 10; i++) {
            XInfo info = cc.testInfo();
            cc.infoJson(info);
        }
        XInfoAll infoAll = cc.testInfoAll();

        cc.infoAllJson(infoAll);
    }
}
