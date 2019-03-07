package com.wsdc.p_j_0.http;

/*
 *  公共数据常量池
 *  <li>    不使用枚举
 *  <li>    不使用枚举
 *  <li>    不使用枚举
 */
public class HttpGK {
    public static final String GET = "GET";
    public static final String POST = "POST";

    public static final int PORT_80 = 80;
    public static final int PORT_443 = 443;

    public static final String HTTP = "http";
    public static final String HTTPS = "https";

    public static final String REQUST_LINE_FORMAT = "%s %s %s";

    //  支持的协议
    public static final String PROTOCOL_HTTP_1_1 = "HTTP/1.1";
    public static final String PROTOCOL_HTTP_2_0 = "HTTP/2.0";

    //  媒体类型
    public static final String CONTENT_TYPE = "Content-Type";

    //  分块传输
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";

    //  分块传输的值  (如果使用该功能，传输下面这个值)
    public static final String CHUNKED = "chunked";

    //  请求体长度
    public static final String CONTENT_LENGTH = "Content-Length";

    //  是否使用压缩格式    (gzip)  如果是大文件(超过4K)可以使用这个头信息
    public static final String CONTENT_ENCODING = "Content-Encoding";

    //  标记主机地址
    public static final String HOST = "host";

    //  标记使用者
    public static final String USER_AGENT = "user_agent";

    public static final String ETAG = "Etag";

    public static final String IF_NOT_MATCH = "If-Not-Match";

    //  缓存控制
    public static final String CACHE_CONTROL = "Cache-Control";

    //  重定向
    public static final String LOCATION = "Location";


}
