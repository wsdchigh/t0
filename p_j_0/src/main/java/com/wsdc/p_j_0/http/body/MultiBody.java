package com.wsdc.p_j_0.http.body;

import com.wsdc.p_j_0.http.RequestBody0;
import com.wsdc.p_j_0.http.io.IO;

/*
 *  多媒体上传
 *  <li>    多种数据格式上传    (文本，文件，etc)
 *  <li>    通常用来上传文件
 *          <li>    字符串等纯文本通常是和文件分开上传的
 */
public class MultiBody extends RequestBody0 {
    @Override
    public int write(IO source) {
        return 0;
    }

    @Override
    public String contentType() {
        return null;
    }

    @Override
    public Object content() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
