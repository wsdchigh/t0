package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.http.io.IByteData;
import com.wsdc.p_j_0.http.io.Segment;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/*
 *  响应
 */
public class Response0 {
    public static final int STATUS_LINE = 0;
    public static final int STATUS_HEADER = 1;
    public static final int STATUS_BODY = 2;

    String responseLine;
    int code;
    String protocol;

    Map<String,String> headers = new HashMap<>();

    ICall call;

    int size = -2;

    //  记录chunk内容的长度
    int chunkSize0 = -1;

    public Response0(ICall call) {
        this.call = call;
    }

    /*
     *  标记解析周期
     *  <li>    0   解析响应行
     *  <li>    1   解析响应头
     *  <li>    2   解析响应体
     */
    int status = 0;

    protected void header(String key,String value){
        headers.put(key,value);
    }

    protected void setResponseLine(String responseLine){
        String[] split = responseLine.split(" ");
        protocol = split[0].trim();
        code = Integer.parseInt(split[1]);
        this.responseLine = responseLine;
    }

    public String getResponseLine() {
        return responseLine;
    }

    protected void headerLine(String headerLine){
        int i = headerLine.indexOf(':');
        if(i == -1){
            throw new RuntimeException("invalid header format ,header = "+headerLine);
        }

        String key0 = headerLine.substring(0,i);
        String value0 = headerLine.substring(++i);

        header(key0.trim().toLowerCase(),value0.trim());
        //System.out.println("key = "+key0+"      value = "+value0);
    }

    /*
     *  解析body内容
     *  <li>    -1  表示结束了
     *  <li>    其他值表示没有结束
     */
    protected int parseBody() throws IOException{
        if(size == -2){
            String contentLength = getHeader("content-length");
            String chunked = getHeader("transfer-encoding");

            //  如果没有contentLength   视为使用chunked编码
            /*
            if(contentLength == null && chunked == null){
                throw new RuntimeException("invalid body ,not have content-length or transfer-encoding header");
            }
            */

            if(contentLength != null){
                size = Integer.parseInt(contentLength);
            }else{
                size = -1;
            }

        }

        //System.out.println("size = "+size);
        if(size > 0){
            //System.out.println("size = "+size+"     now = "+call.sink().size());
            int readSize = call.sink().size();
            if(readSize == this.size){
                return -1;
            }

            //System.out.println("readSize = "+readSize+"     origin = "+size);
        }

        if(size == -1){
            /*
             *  关于分块编码
             *  <li>    长度  \r\n
             *  <li>    内容  \r\n
             *          .
             *          .
             *          .
             *  <li>    0   \r\n    (这个分块表示结束)
             *  <li>    \r\n
             *
             *  注意事项    长度已经所有的结尾\r\n均无效
             *  <li>    sink只是临时数据(所有来自服务器的数据)
             *  <li>    buffer充当帮运工
             *  <li>    buffer1是最终有效数据的存储位置
             *
             *  <li>    chunkSize
             *          <li>    -1表示需要读取块的大小
             *          <li>    >0表示需要读取块的长度
             *          <li>    =0表示已经是最后一块了
             *
             */

            if(chunkSize0 == -1){
                if(call.sink().readLine(call.buffer()) != -1){
                    String string = call.buffer().string();
                    System.out.println("origin = "+string);
                    chunkSize0 = Integer.parseInt(string.trim(),16);
                    System.out.println("size0 = "+chunkSize0);
                }
            }

            if(chunkSize0 > 0){
                if(call.sink().size() >= (chunkSize0 + 2)){
                    call.sink().sink(call.buffer1(),chunkSize0);
                    chunkSize0 = -1;
                    call.sink().sink(call.buffer(),2);
                    byte[] bytes = call.buffer().bytes();
                    System.out.println("------------"+bytes[0]+"    "+bytes[1]);
                }
            }

            if(chunkSize0 == 0){
                call.sink().sink(call.buffer());
                if("\r\n".equals(call.buffer().string())){
                    System.out.println("读取完毕");
                }
                return -1;
            }
        }

        return 0;
    }

    public String getHeader(String key){
        return headers.get(key);
    }

    public Map<String,String> headers(){
        return headers;
    }

    public ICall call(){
        return call;
    }

    /*
     *  如果响应头读取完毕
     *  <li>    那么可以通过以下三种方式获取数据
     *  <li>    string  返回一个字符串
     *  <li>    bytes   返回一个byte数组
     *  <li>    inputStream 返回一个流
     *          <li>
     *
     *  <li>
     */
    public String string(){
        return "";
    }

    public byte[] bytes(){
        return new byte[0];
    }

    public InputStream inputStream(){
        return null;
    }

    /*
     *  缓冲过程
     *  <li>    数据先从网络读取到sink
     *  <li>    在将数据从sink读取到segment
     *  <li>    期间缓冲数组越小，那么读取的过程就快
     *
     *  <li>    减少占用空间
     */
    public int readFromNet(IByteData sink) throws IOException {
        return -1;
    }

    public int readFromSink(IByteData sink,Segment segment) throws IOException{
        return -1;
    }
}
