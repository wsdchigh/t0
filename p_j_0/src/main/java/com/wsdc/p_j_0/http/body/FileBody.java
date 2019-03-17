package com.wsdc.p_j_0.http.body;

import com.wsdc.p_j_0.http.HttpGK;
import com.wsdc.p_j_0.http.ICall;
import com.wsdc.p_j_0.http.RequestBody0;
import com.wsdc.p_j_0.http.io.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/*
 *  使用 multipart/mix    上传文件
 *  <li>    只用来上传你文件
 *  <li>    通过固定长度计算长度  (不一次性读取到内存之中)
 *
 *  <li>    不带有其他header，一律文件后缀来标识
 *
 *  <li>    每一个part均是一个小body
 *
 *
 */
public class FileBody extends RequestBody0 {
    List<BodyWrap> list = new LinkedList<>();
    String boundary = new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    String boundary1 = "--"+boundary;
    int index = 0;

    @Override
    protected void setCall(ICall call) throws IOException {
        super.setCall(call);

    }

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
            source.source(("--"+boundary+"--\r\n\r\n").getBytes());
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
        int size = 0;
        for (BodyWrap wrap : list) {
            size += wrap.size();
        }

        //  --  -- \r\n \r\n 总共8个字符
        size += (boundary.length()+8);

        return size;
    }

    public static class Builder{
        FileBody body = new FileBody();
        public Builder addFile(String key,File file,String fileName){
            body.list.add(new BodyWrap(body.boundary1,key,fileName,file));
            return this;
        }
        public FileBody build(){
            return body;
        }
    }

    private static class BodyWrap{
        String name;
        String fileName;
        File file;
        String boundary;
        byte[] disposition;
        FileInputStream fis;

        int step = 0;
        StringBuilder sb = new StringBuilder();

        public BodyWrap(String boundary,String name, String fileName, File file) {
            if(boundary == null || name == null){
                throw new RuntimeException("...............");
            }

            if(file == null ){
                throw new RuntimeException("content == null");
            }
            this.boundary = boundary;
            this.name = name;
            this.fileName = fileName;
            this.file = file;

            sb.append(boundary)
                    .append("\r\n");

            sb.append("Content-Disposition: form-data;")
                    .append("name=")
                    .append('\"')
                    .append(name)
                    .append('\"');

            if(fileName != null && fileName.length() > 0){
                sb.append(";filename=")
                        .append('\"')
                        .append(fileName)
                        .append('\"');
            }

            sb.append("\r\nContent-Length:")
                    .append(file.length());

            System.out.println("file length = "+file.length());

            sb.append("\r\n\r\n");

            disposition = sb.toString().getBytes();
            sb.replace(0,sb.length(),"");
        }

        /*
         *  读数据
         *  <li>    -1  读完了
         *  <li>    1   没有读完
         */
        public int write(IO io) throws IOException {
            if(step++ == 0){
                sb.replace(0,sb.length(),"");
                io.source(disposition);
            }

            if(fis == null){
                fis = new FileInputStream(file);
            }

            if(fis.available() == 0){
                fis.close();
                io.source(new byte[]{'\r','\n'});
                //io.source(new byte[]{-1});
                return -1;
            }
            io.source(fis);
            return 0;
        }

        int size = -1;

        public int size(){
            //  不需要拼接\r\n    这两个字符标识结束
            if(size == -1){
                size = (int) (disposition.length + file.length())+2;
            }
            System.out.println("file.size = "+file.length());
            return size;
        }
    }
}
