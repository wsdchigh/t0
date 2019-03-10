package com.wsdc.p_j_0.http;

import com.wsdc.p_j_0.looper.LCall;
import com.wsdc.p_j_0.looper.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*
 *  链接
 *  <li>    连接，我们只需要InputStream/OutputStream
 *  <li>    里面如何实现(Socket)不需要暴露
 *
 *  <li>    这里是具体的连接
 *
 *  <li>    连接可以无法读写任何数据
 */
public class Connection implements LCall {
    private String host;
    private int port;
    private Socket socket;
    private SSLSocket sslSocket;
    private ICall call;
    private Looper looper;
    private Client client;
    private InputStream inputStream;
    private OutputStream outputStream;

    private ConnectionPool.Address address;

    //  记录缓存的空闲时间
    long spareTime = 0l;
    /*
     *  标记状态值
     *  <li>    0   初始状态    (未连接)
     *  <li>    1   准备连接    (开始连接，但是没有获取连接结果)
     *  <li>    2   连接成功    (结果显示连接成功)
     *  <li>    -1  连接失败    (结果显示连接失败)
     *  <li>    3   IO          (开始IO读取)
     *  <li>    -3  IO结束      (结束IO操作)
     */
    int status = 0;

    public static final int STATUS_INIT = 0;
    //public static final int STATUS_PREPARE = 1;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_CONNECTED_OK = 2;
    public static final int STATUS_CONNECTED_FAILURE = -1;
    public static final int STATUS_CONNECTED_IO = 3;
    public static final int STATUS_CONNECTED_IO_END = -3;

    public Connection(Client client, ConnectionPool.Address address) {
        this.client = client;
        this.address = address;
        looper = client.looper;
    }

    public InputStream inputStream() throws IOException {
        return inputStream;
    }

    public OutputStream outputStream() throws IOException {
        return outputStream;
    }

    public void setCall(ICall call) {
        this.call = call;
        host = call.request().host;
        port = call.request().port;
    }

    public ICall getCall() {
        return call;
    }

    /*
     *  ping网络是否可以连通
     *  <li>    SO_OOBINLINE    紧急数据包   (默认不开启)
     *          <li>    该标记发送的消息属于紧急消息，不会影响到征程IO流操作
     *          <li>    如果服务Socekt需要使用紧急数据，那么不会是正常IO流来处理这些数据
     *  <li>    我们可以选择ping一次
     *          <li>    判断网络是否连接正常
     *          <li>    在需要些数据的时候ping一次即可
     *          <li>    尽量别ping
     */
    public boolean ping() {
        boolean rtn = false;
        try {
            socket.sendUrgentData(0xff);
            rtn = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    /*
     *  连接需要使用连接线程
     *  <li>    使用连接线程去连接
     *  <li>    连接线程可以是IO线程 (尽量不要这样)
     *
     *  <li>    根据是否为ssl决定使用哪个socket
     */
    public void connect() throws IOException {
        if("http".equals(call.request().protocol)){
            socket = new Socket(host, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        }else{
            try {
                File file = new File("E:\\al\\p_j_0\\src\\main\\java\\wsdc.jks");
                System.out.println(file.getAbsoluteFile());
                KeyStore keyStore = KeyStore.getInstance("pkcs12");
                FileInputStream fis = new FileInputStream(file);
                keyStore.load(fis,"wsdc1993".toCharArray());

                KeyManagerFactory factory0 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                factory0.init(keyStore,"wsdc1993".toCharArray());
                KeyManager[] keyManagers = factory0.getKeyManagers();


                SSLContext context = SSLContext.getInstance("SSL", "SunJSSE");
                context.init(keyManagers,new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }},new SecureRandom());

                SSLSocketFactory factory = context.getSocketFactory();

                socket = factory.createSocket(host, port);
                ((SSLSocket) socket).startHandshake();
                //System.out.println("host = "+host+"     port = "+port);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                /*
                String s = "GET / HTTP/1.1\r\n" +
                        "host:www.baidu.com\r\n\r\n";
                outputStream.write(s.getBytes());
                outputStream.flush();
                System.out.println(s);

                byte[] data= new byte[2048];

                int read = inputStream.read(data);
                System.out.println(new String(data,0,read));
                */
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }  catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        socket.setSoTimeout(5000);

    }

    public int status() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void toQueue() throws Exception {
        status = STATUS_CONNECTED_IO;
    }

    @Override
    public boolean loop() throws Exception {
        /*
         *  IO先后顺序
         *  <li>    只有request写完了，才能读取response
         */
        if (call != null) {
            //  一定要控制这个返回值  如果有任何写入一定要返回true
            boolean rtn = false;
            switch (call.status()) {
                case ICall.STATUS_REQUEST:
                    int l1 = call.request().write(call.source());
                    int l2 = call.source().sink(outputStream);
                    //int l2 = call.source().sink(call.buffer());
                    if(l1 == -1 && l2 == 0){

                        call.setStatus(ICall.STATUS_RESPONSE_HEADER);
                        if(needReadBefore()){
                            int read = inputStream.read();

                            //  sslSocket inputStream.available() == 0 可以能回出现这种状态
                            //  需要率先读取一个byte    (阻塞)
                            call.sink().source(new byte[]{(byte) (read&0xff)});
                        }
                    }
                    outputStream.flush();

                    rtn = true;
                    break;

                case ICall.STATUS_RESPONSE_HEADER:
                    /*
                     *  读取请求头头
                     *  <li>    一次会从流中读取64位，但是一行可能没有64位
                     *          会存在以下情况
                     *          <li>    流中的数据已经读完了，但是header还没有解析完毕
                     *          <li>    如果body中的数据比较少，那么可能body读已经读完了
                     *
                     */
                    //System.out.println("size = "+inputStream.available());
                    //System.out.println(inputStream.getClass().getName());
                    if(inputStream.available() > 0){
                        call.sink().source(inputStream);
                        rtn = true;
                    }
                    if(call.sink().size() > 0){
                        int line = -1;
                        line = call.sink().readLine(call.buffer());
                        if(line != -1){
                            String string = call.buffer().string();
                            System.out.println(string);
                            if("\n".equals(string) || "\r\n".equals(string)){
                                call.setStatus(ICall.STATUS_RESPONSE_BODY);
                                int i = call.response().parseBody();
                                if(i == -1){
                                    //  避免body此时已经读完了，所以需要先行判断一次
                                    System.out.println(">>>>>>>>>>>>>>>>>");
                                    call.setStatus(ICall.STATUS_END);
                                }
                            }else{
                                if(call.response().responseLine == null){
                                    call.response().setResponseLine(string);
                                }else{
                                    call.response().headerLine(string);
                                }
                            }
                        }
                    }

                    break;

                case ICall.STATUS_RESPONSE_BODY:
                    /*
                     *  先读一个字节  (通常是会阻塞的)
                     *  <li>    ssl,chunked会让inputStream.available()一直 == 0
                     */
                    if(inputStream.available() == 0){
                        if(needReadBefore()){
                            int read = inputStream.read();
                            call.sink().source(new byte[]{(byte) (read&0xff)});
                        }
                    }

                    call.sink().source(inputStream);
                    int i = call.response().parseBody();
                    if(i == -1){
                        call.setStatus(ICall.STATUS_END);
                    }
                    rtn = true;
                    break;

                case ICall.STATUS_END:
                    //  不要在这里处理成功处理
                    //  移动到移除队列的时候再行处理
                    requestExit();
                    rtn = true;
                    break;
            }
            return rtn;
        }
        return false;
    }


    @Override
    public void exception(int status0, Exception e) {
        status = STATUS_CONNECTED_IO_END;

        //  直接失败
        requestExit();
    }

    /*
     *  同一个地址+port最多缓存2个空闲连接
     *  <li>    如果需要退出，调用这个函数
     *  <li>    不要在这里关闭任何数据
     */
    @Override
    public void requestExit() {
        looper.unregister(this);
    }

    @Override
    public void exitQueue() throws Exception {
        if (status != STATUS_CONNECTED_IO_END) {
            if (address.queue.size() < address.capacity) {
                address.queue.offer(this);
                spareTime = System.currentTimeMillis();
            }

            System.out.println("读取结束");
            System.out.println(call.sink().string());
            System.out.println(call.buffer1().string());
            call.finish();
            call = null;
            status = ICall.STATUS_REQUEST;
        } else {
            close();
            call.finish();
        }
    }

    public void close() {
        try {
            if (inputStream != null) {
                inputStream().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (outputStream != null) {
                outputStream().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String https = "https";
    private boolean needReadBefore(){
        return https.equals(call.request().protocol);
    }
}
