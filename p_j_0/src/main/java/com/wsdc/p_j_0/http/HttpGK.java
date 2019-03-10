package com.wsdc.p_j_0.http;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 *  公共数据常量池
 *  <li>    不使用枚举
 *  <li>    不使用枚举
 *  <li>    不使用枚举
 */
public class HttpGK {
    public static Map<String,String> defaultMap = new HashMap<>();

    static{
        defaultMap.put("Transfer-Encoding","chunked");
    }

    public static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    public static String getTime(){
        Date date = new Date(System.currentTimeMillis());
        String format = timeFormat.format(date);
        return format;
    }


    public static final String GET = "GET";
    public static final String POST = "POST";

    public static final int PORT_80 = 80;
    public static final int PORT_443 = 443;

    public static final String HTTP = "http";
    public static final String HTTPS = "https";

    public static final String REQUEST_LINE_FORMAT = "%s %s %s";

    //  支持的协议
    public static final String PROTOCOL_HTTP_1_1 = "HTTP/1.1";
    public static final String PROTOCOL_HTTP_2_0 = "HTTP/2.0";

    /*
     *  上行和下行是有区别的
     *  <li>    上行通常是固定了几种格式
     *          <li>    上行同时是表单和json
     *  <li>    下行通常是不固定的
     */
    public static class ContentType{
        //  表单  (常用)
        public static final String FORM = "Content-Type : application/x-www-form-urlencoded";

        //  json
        public static final String JSON = "Content-Type : application/json";

        //  多媒体文本   (上传文件)
        public static final String MULTIPART = "Content-Type : multi/form-data";

    }

    /*
     *  对requestBody的限定
     */
    public static class ContentLength{
        public static final String LENGTH = "Content-Length : ";

        //  分块编码固定这种格式
        public static final String ENCODING = "Transfer-Encoding : chunked";
    }

    /*
     *  缓存控制    关于缓存的几个标签
     */
    public static class CacheControl{
        public static final String NO_CACHE = "Cache-Control : no-cache";
        public static final String MAX_AGE = "Cache-Control : max-age=";

        /*
         *  Etag和if-none-match是成对出现的
         */
        public static final String ETAG = "Etag : ";
        public static final String IF_NONE_MATCH = "If-None-Match : ";
    }

    /*
     *  如果需要断点续传
     *  <li>    判断原始文件的长度
     *  <li>    长度从0开始，含头含尾 (一定注意0-499才表示500个自己)
     *
     *  <li>    如果服务器支持range参数，那么返回的是206    不是200
     */
    public static class RANGE{
        /*
         *  指定区间
         */
        public static final String RANGE0 = "Range : bytes=%d-%d";

        /*
         *  指定开头
         *  <li>    通常使用这种方式
         */
        public static final String RANGE1 = "Range : bytes=%d-";

        /*
         *  指定结尾
         *  <li>
         */
        public static final String RANGE2 = "Range : bytes=-%d";

        //  服务器允许接收range
        public static final String ACCEPT_RANGES = "Accept-Ranges : bytes";

        //  服务器不支持
        public static final String ACCEPT_RANGES_NONE = "Accept-Ranges : none";
    }

    /*
     *  是否长链接
     *  <li>    1.1默认长链接
     */
    public static class Connection{
        public static final String KEEP_ALIVE = "Connection : Keep-Alive";
        public static final String CLOSE = "Connection : Close";
    }

    /*
     *  通用请求头
     */
    public static class Common{
        public static final String HOST = "Host : ";
        public static final String DATE = "Date : ";
        public static final String USER_AGENT = "User-Agent : wsdchigh-1.0.0";
        public static final String ACCEPT_ENCODING = "Accept-Encoding : gzip";
    }
}
