package com.wsdc.p_j_0.http.body;

import com.wsdc.p_j_0.http.RequestBody0;
import com.wsdc.p_j_0.http.io.IO;

/*
 *  使用json上传数据
 */
public class JsonBody extends RequestBody0 {
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
