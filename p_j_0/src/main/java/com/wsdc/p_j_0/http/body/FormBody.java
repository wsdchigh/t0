package com.wsdc.p_j_0.http.body;

import com.wsdc.p_j_0.http.HttpGK;
import com.wsdc.p_j_0.http.ICall;
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
        if(call.buffer().size() > 0){
            call.source().source(call.buffer());
            return 0;
        }
        return -1;
    }

    @Override
    public String contentType() {
        return HttpGK.ContentType.FORM;
    }

    @Override
    protected void setCall(ICall call) throws IOException {
        super.setCall(call);
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

        if(sb.length() == 0){
            return;
        }
        String s = sb.substring(0,sb.length()-1);
        call.buffer().source(s.getBytes());
        size = call.buffer().size();
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
            body.form.put(key,value);
            return this;
        }

        public Builder add(Map<String,String> maps){
            body.form.putAll(maps);
            return this;
        }

        public FormBody build(){
            return body;
        }
    }
}
