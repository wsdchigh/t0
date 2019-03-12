package com.wsdc.p_j_0.http.body;

import com.wsdc.p_j_0.http.HttpGK;
import com.wsdc.p_j_0.http.RequestBody0;
import com.wsdc.p_j_0.http.io.IO;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/*
 *  多媒体上传
 *  <li>    多种数据格式上传    (文本，文件，etc)
 *  <li>    通常用来上传文件
 *          <li>    字符串等纯文本通常是和文件分开上传的
 *
 *  <li>    三种格式
 *          <li>    字符串(纯文本)
 *          <li>    文件
 *          <li>    表单  表单是一种特殊的纯文本
 *
 *  <li>    需要边界
 *          <li>    ;boundary=avdfewedfhwefhuehdjfhuw        (任意字符，前面最好是有多个-符号)
 *          <li>    这个分解符在之后的过程中  唯一
 *          <li>    边界标识符尽量复杂一定  不要和正文内容出现一样    ()
 *
 *          <li>    最有有一个结尾 (下一行是\r\n)
 */
public class MultiBody extends RequestBody0 {
    List<BodyWrap> list = new LinkedList<>();
    String boundary = new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    String boundary1 = "--"+boundary;
    int index = 0;

    @Override
    public int write(IO source) throws IOException{
        if(index < list.size()){
            int write = list.get(index).write(source);
            if(write == -1){
                //System.out.println("index = "+index+"   size = "+list.size());
                index++;
            }

            return 1;
        }else if(index == list.size()){
            source.source((boundary1+"--").getBytes());
            source.source(new byte[]{'\r','\n','\r','\n'});
            System.out.println("结束了");
            index++;
        }

        return -1;
    }

    @Override
    public String contentType() {
        return HttpGK.ContentType.MULTIPART+";boundary="+boundary;
    }

    @Override
    public Object content() {
        return null;
    }

    @Override
    public int size() {
        return -2;
    }

    public static class Builder{
        MultiBody body = new MultiBody();

        //  不要添加string  并不是所有的服务器均支持这些功能  通常就是用来歘那图片的
        public Builder addString(String key,String value){
            body.list.add(new BodyWrap(body.boundary1,key,null,null,value,null));
            return this;
        }

        public Builder addFile(String key,File file,String fileName){
            body.list.add(new BodyWrap(body.boundary1,key,fileName,file,null,null));
            return this;
        }

        public Builder addForm(String key,String form){
            body.list.add(new BodyWrap(body.boundary1,key,null,null,null,form));
            return this;
        }

        public MultiBody build(){
            return body;
        }
    }

    private static class BodyWrap{
        String name;
        String fileName;
        File file;
        String text;
        String form;

        String boundary;

        int step = 0;
        StringBuilder sb = new StringBuilder();

        public BodyWrap(String boundary,String name, String fileName, File file, String text,String form) {
            if(boundary == null || name == null){
                throw new RuntimeException("...............");
            }

            if(file == null && text == null && form == null){
                throw new RuntimeException("content == null");
            }
            this.boundary = boundary;
            this.name = name;
            this.fileName = fileName;
            this.file = file;
            this.text = text;
            this.form = form;
        }

        /*
         *  读数据
         *  <li>    -1  读完了
         *  <li>    1   没有读完
         */
        public int write(IO io) throws IOException {
            sb.replace(0,sb.length(),"");
            if(text != null){
                byte[] bytes = text.getBytes();
                if(step++ == 0){
                    sb.append(boundary)
                            .append("\r\n")
                            .append("Content-Disposition: form-data;")
                            .append("name=")
                            .append('\"')
                            .append(name)
                            .append("\"\r\n")
                            .append("Content-Length:")
                            .append(bytes.length)
                            .append("\r\n\r\n");
                    io.source(sb.toString().getBytes());
                }
                io.source(bytes);
                io.source(new byte[]{'\r','\n'});
                return -1;
            }else if(file != null){
                byte[] bytes = "1234567890".getBytes();
                if(step++ == 0){
                    sb.append(boundary)
                            .append("\r\n")
                            .append("Content-Disposition: form-data;")
                            .append("name=")
                            .append('\"')
                            .append(name)
                            .append(";filename=")
                            .append(file.getName())
                            .append("\"\r\n")
                            .append("Content-Type:")
                            .append("plain/text")
                            .append("\r\n\r\n");
                    io.source(sb.toString().getBytes());
                }

                io.source("1234567890".getBytes());
                return -1;
            }else if(form != null){
                if(step++ == 0){

                }
            }

            return -1;
        }
    }
}
