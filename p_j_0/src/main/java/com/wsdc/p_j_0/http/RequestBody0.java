package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.io.IO;

import java.io.IOException;

/*
 *  请求体
 *  <li>    只有表单和json使用 (Content-Length)
 *          <li>    其他情况一律使用    transfer-encoding:chunked   (使用分块编码)
 */
public abstract class RequestBody0 {
    Request0 request;
    protected ICall call;

    /*
     *  将数据写入到源中
     *  <li>    这个函数是会多次调用的
     */
    public abstract int write(IO source) throws IOException;

    /*
     *  不同的请求体，contentType是不一样的
     *  <li>    不同的contentType，对数据的封装也是不一样的
     *  <li>    具体支持的请求头信息 存储在HttpGK之中
     */
    public abstract String contentType();

    //  返回具体的信息
    public abstract Object content();

    /*
     *  用于生成contentLength请求头信息
     *  <li>    -1  表示不使用ContentLength请求头
     *              使用chunked
     *
     *  <li>    0   表示请求体根本没有任何数据(空的)
     *
     *  <li>    >0  存在数据
     */
    public abstract int size();

    protected void setCall(ICall call) throws IOException {
        this.call = call;
        this.request = call.request();
    }

    public Request0 request(){
        return request;
    }

    public static final RequestBody0 EMPTY_BODY = new RequestBody0() {
        @Override
        public int write(IO source) throws IOException {
            return -1;
        }

        @Override
        public String contentType() {
            return HttpGK.ContentType.FORM;
        }

        @Override
        public Object content() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }
    } ;

    //  使用这种方式传json create("application/json","{"a":1234546}")
    public static RequestBody0 create(final String contentType, final String content){
        RequestBody0 body = new RequestBody0() {
            @Override
            protected void setCall(ICall call) throws IOException {
                super.setCall(call);
                call.buffer().source(content.getBytes());
            }

            @Override
            public int write(IO source) throws IOException {
                if(call.buffer().size() > 0 ){
                    call.source().source(call.buffer());
                    return 0;
                }
                return -1;
            }

            @Override
            public String contentType() {
                return contentType;
            }

            @Override
            public Object content() {
                return content;
            }

            @Override
            public int size() {
                return call.buffer().size();
            }
        };


        return body;
    }
}
