package com.wsdchigh.g_a_rely.log;

public class LogBean {
    /*
     *  type    (类型)
     *  <li>    debug
     *  <li>    verbose
     *  <li>    exception
     *  <li>    trace
     *
     */
    public String type;

    //  记录当前时间
    public long time;

    //  标记一个tag
    public String tag;

    //  具体记录的内容
    public String record;

    //  描述手机信息  (型号，品牌) 用于异常跟踪，那么机器出现了问题
    public String mobile;

    //  用户  (如果用户登录，记录登录的用户)
    public String user;

    //  ip    (记录用户IP)
    public String ip;

    //  位置信息    (记录用户所处的位置信息)
    public String location;

    //  其他信息
    public Object other;
}
