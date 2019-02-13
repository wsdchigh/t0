package com.wsdc.file;

import com.wsdc.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    public static InputStream inputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public static OutputStream outputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    /*
     * 删除文件
     * <li>    删除文件
     * <li>    删除文件夹
     */
    public static void delete(File file){
        if(file.isFile()){
            file.delete();
            return;
        }else{
            File[] files = file.listFiles();
            for (File f1 : files) {
                delete(f1);
            }
            file.delete();
        }
    }

    /*
     *  复制文件
     *  <li>    将src中的内容全部复制一份到dest目录下面
     *          <li>    最终dest中包含src    (上下级关系)
     *
     *  <li>    path    调用的时候，传递null参数
     */
    public static void copy(File src,File dest,String path){
        if(path == null){
            path = src.getParentFile().getAbsolutePath();
            if(!path.endsWith("/")){
                path = path + "/";
            }
        }

        if(src.isFile()){
            final String p0 = src.getAbsolutePath();
            final String p1 = p0.replace(path,"");
            File newFile = new File(dest,p1);

            try {
                IOUtils.write(FileUtils.inputStream(src),FileUtils.outputStream(newFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            final String p0 = src.getAbsolutePath();
            final String p1 = p0.replace(path,"");
            File newFile = new File(dest,p1);

            newFile.mkdirs();

            File[] files = src.listFiles();
            for (File file : files) {
                copy(file,dest,path);
            }
        }

    }

    /*
     *  移动文件
     *  <li>    us ud 请传递null
     */
    public static void rename(File src,File dest,String us,String ud){
        if(dest.exists()){
            return;
        }

        if(us == null){
            us = src.getAbsolutePath();
            ud = dest.getAbsolutePath();
        }

        String s0 = src.getAbsolutePath();
        s0 = s0.replace(us, ud);

        src.renameTo(new File(s0));

        if(src.isDirectory()){
            File[] files = src.listFiles();

            for (File file : files) {
                rename(file,dest,us,ud);
            }
        }
    }
}
