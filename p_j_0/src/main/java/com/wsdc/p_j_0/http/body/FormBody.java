package com.wsdc.p_j_0.http.body;

import com.wsdc.p_j_0.http.Request0;
import com.wsdc.p_j_0.http.RequestBody0;
import com.wsdc.p_j_0.http.io.IO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 *  普通文本表单
 */
public class FormBody extends RequestBody0 {
    Map<String,String> form = new HashMap<>();
    int step = 0;
    int size = 0;

    @Override
    public int write(IO source) throws IOException {
        if(bodyIO.size() > 0){
            source.source(bodyIO);
            return 1;
        }
        return -1;
    }

    @Override
    public String contentType() {
        return "Content-Length : "+size();
    }

    @Override
    protected void setRequest(Request0 request) throws IOException {
        super.setRequest(request);
        StringBuilder sb = new StringBuilder();
        Set<String> set = form.keySet();
        for (String s : set) {
            String value = form.get(s);

            if(value != null && !value.equals("")){
                sb.append(s)
                        .append("=")
                        .append(value)
                        .append("&");
            }
        }

        String s = sb.substring(0,sb.length()-1);
        bodyIO.source(s.getBytes());
        size = bodyIO.size();
    }

    @Override
    public Object content() {
        return form;
    }

    @Override
    public int size() {
        return size;
    }

    public static class Builder{
        FormBody body = new FormBody();

        public Builder add(String key,String value){
            return this;
        }

        public Builder add(Map<String,String> maps){
            return this;
        }

        public FormBody build(){
            return body;
        }
    }
}
