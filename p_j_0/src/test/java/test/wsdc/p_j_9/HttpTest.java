package test.wsdc.p_j_9;

import com.wsdc.p_j_0.http.Call0;
import com.wsdc.p_j_0.http.Client;
import com.wsdc.p_j_0.http.HttpGK;
import com.wsdc.p_j_0.http.ICall;
import com.wsdc.p_j_0.http.Request0;
import com.wsdc.p_j_0.http.RequestBody0;
import com.wsdc.p_j_0.http.body.FileBody;
import com.wsdc.p_j_0.http.body.FormBody;
import com.wsdc.p_j_0.http.io.IO;
import com.wsdc.p_j_0.http.io.IOs;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpTest {
    @Test
    public void testRequest(){
        Client client = new Client();

        Request0.Builder requestBuilder = new Request0.Builder();
        Request0 request = requestBuilder.url("http://img.wsdchigh.site/index.html#/home")
                .method("GET")
                .build();

        ICall call = new Call0(request,client);

        try {
            while((request.write(call.source())) != -1){
                String string = call.source().string();
                Socket socket = new Socket("img.wsdchigh.site",80);
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                outputStream.write(string.getBytes());
                outputStream.flush();

                byte[] data = new byte[1024];
                int read = inputStream.read(data);
                System.out.println(new String(data,0,read));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testHttp0(){
        Client client = new Client();

        /*
        Request0.Builder requestBuilder = new Request0.Builder();
        Request0 request = requestBuilder.url("http://img.wsdchigh.site/index.html#/home")
                .method("GET")
                .build();

        try {
            client.call(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.currentThread().sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

*/
        Request0.Builder requestBuilder1 = new Request0.Builder();
        Request0 request1 = requestBuilder1.url("http://www.huya.com/")
                .method("GET")
                .build();

        try {
            client.call(request1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.currentThread().sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHttps(){
        Client client = new Client();

        if(true){
            Request0.Builder requestBuilder1 = new Request0.Builder();
            Request0 request1 = requestBuilder1.url("http://www.baidu.com/")
                    .method("GET")
                    .header("User-Agent","wsdchigh")
                    .build();

            try {
                client.call(request1, new ICall.ICallback() {
                    @Override
                    public boolean success(ICall call) throws IOException {
                        System.out.println(call.response().string());
                        return false;
                    }

                    @Override
                    public void failure(ICall call, Exception e) throws IOException {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.currentThread().sleep(3000);
            if(false){
                Request0.Builder requestBuilder1 = new Request0.Builder();
                Request0 request1 = requestBuilder1.url("https://www.huya.com/")
                        .method("GET")
                        .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding","gzip, deflate, br")
                        .header("Accept-Language","zh-CN,zh;q=0.9")
                        .header("Cache-Control","no-cache")
                        .header("Connection","keep-alive")
                        .header("Pragma","no-cache")
                        .header("Upgrade-Insecure-Requests","1")
                        .header("User-Agent","wsdchigh")
                        .build();

                try {
                    client.call(request1, new ICall.ICallback() {
                        @Override
                        public boolean success(ICall call) throws IOException {
                            System.out.println(call.response().string());
                            return false;
                        }

                        @Override
                        public void failure(ICall call, Exception e) throws IOException {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Thread.currentThread().sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //  测试chunk
    @Test
    public void testChunk(){
        String url = "https://image.baidu.com/search/detail?" +
                "ct=503316480&z=0&ipn=d&word=%E7%99%BE%E5%BA%A6%E5%9B%BE%E7%89%87&hs=2&pn=0" +
                "&spn=0&di=233970&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2" +
                "&lm=-1&cs=1690714227%2C261409767&os=698326255%2C2770532192&simid=0%2C0&adpicid=0&lpn=0" +
                "&ln=30&fr=ala&fm=&sme=&cg=&bdtype=0" +
                "&oriquery=%E7%99%BE%E5%BA%A6%E5%9B%BE%E7%89%87&objurl=http%3A%2F%2Fhbimg.b0.upaiyun.com%" +
                "2F28a4962c297205e0868cdb45bb527e2bc5319f08f019-l7N1A3_fw658&fromurl=ippr_z2C%24qAzdH3F" +
                "AzdH3Fi7wkwg_z%26e3Bv54AzdH3FrtgfAzdH3F8a0dlm8a8lAzdH3F&gsm=0&islist=&querylist=";

        String s = "http://www.itkeyword.com/doc/3193114968827715690/what-does-inputstream-available-do-in-java";
        Client client = new Client();
        Request0.Builder requestBuilder = new Request0.Builder();
        Request0 request = requestBuilder.url(url)
                .method("GET")
                .build();



        try {
            client.call(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.currentThread().sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //  模拟访问百度
    @Test
    public void testHttpsBaidu(){
        try{
            System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
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
            }},null);
            SSLSocketFactory factory = context.getSocketFactory();

            SSLSocket socket = (SSLSocket) factory.createSocket("www.baidu.com", 443);
            socket.startHandshake();

            String s = "GET / HTTP/1.1\r\n" +
                    "host:www.baidu.com\r\n\r\n";

            socket.getOutputStream().write(s.getBytes());
            socket.getOutputStream().flush();

            Thread.currentThread().sleep(1000);
            byte[] data= new byte[1024];
            int read = socket.getInputStream().read(data);

            System.out.println(new String(data,0,read));

            socket.close();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    //  模拟一个https 客户端
    @Test
    public void testCreateHttpsClient(){
        try{
            System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
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
            }},null);
            SSLSocketFactory factory = context.getSocketFactory();

            SSLSocket socket = (SSLSocket) factory.createSocket("127.0.0.1", 444);
            socket.startHandshake();

            socket.getOutputStream().write("我爱北京天安门".getBytes());
            socket.getOutputStream().flush();

            Thread.currentThread().sleep(1000);
            byte[] data= new byte[1024];
            int read = socket.getInputStream().read(data);

            System.out.println(new String(data,0,read));

            socket.close();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    //  模拟一个https服务端
    @Test
    public void testCreateHttpsServer(){
        try {
            System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
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
            }},null);
            SSLServerSocketFactory factory = context.getServerSocketFactory();

            ServerSocket serverSocket = factory.createServerSocket(444);

            System.out.println("11");
            Socket accept = serverSocket.accept();
            System.out.println("22");

            byte[] data = new byte[1024];
            int read = accept.getInputStream().read(data);

            accept.getOutputStream().write("我不是黄蓉".getBytes());
            accept.getOutputStream().flush();
            System.out.println(new String(data,0,read));

            Thread.sleep(4000);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLocal(){
        Client client = new Client();

        Request0 request = new Request0.Builder()
                .url("http://127.0.0.1:8080/a.do?api=test")
                .method("GET")
                .build();

        try {
            client.call(request, new ICall.ICallback() {
                @Override
                public boolean success(ICall call) throws IOException{
                    System.out.println(call.response().string());
                    return false;
                }

                @Override
                public void failure(ICall call, Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPost(){
        Client client = new Client();

        FormBody body = new FormBody.Builder()
                .add("username", "wsdchigh")
                .add("password", "wsdc1993")
                .build();
        Request0 request = new Request0.Builder()
                .url("http://127.0.0.1:8080/a.do?api=test_post")
                .method("POST")
                .body(body)
                .build();

        try {
            client.call(request, new ICall.ICallback() {
                @Override
                public boolean success(ICall call) throws IOException{
                    System.out.println(call.response().string());
                    return false;
                }

                @Override
                public void failure(ICall call, Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJson(){
        Client client = new Client();
        Request0 request = new Request0.Builder()
                .url("http://127.0.0.1:8080/a.do?api=test_json")
                .method("POST")
                .body(RequestBody0.create(HttpGK.ContentType.JSON,"{\"a\":12345}"))
                .build();

        try {
            client.call(request, new ICall.ICallback() {
                @Override
                public boolean success(ICall call) throws IOException{
                    System.out.println(call.response().string());
                    return false;
                }

                @Override
                public void failure(ICall call, Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMulti(){
        Client client = new Client();
        File file1 = new File("C:\\Users\\wsdchigh\\Desktop\\img1\\a1.png");
        File file2 = new File("C:\\Users\\wsdchigh\\Desktop\\img1\\a2.jpg");
        File file3 = new File("C:\\Users\\wsdchigh\\Desktop\\img1\\a3.png");
        FileBody body = new FileBody.Builder()
                .addFile("files1",file1,file1.getName())
                .addFile("files2",file2,file2.getName())
                .addFile("files3",file3,file3.getName())
                .build();

        Request0 request = new Request0.Builder()
                .url("http://192.168.1.102:8080/a.do?api=test_multi")
                .method("POST")
                .body(body)
                .build();

        try {
            client.call(request, new ICall.ICallback() {
                @Override
                public boolean success(ICall call) throws IOException{
                    System.out.println(call.response().string());
                    return false;
                }

                @Override
                public void failure(ICall call, Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testMultiServe(){
        try {
            ServerSocket ss = new ServerSocket(8888);
            int i = 0;
            Socket accept = ss.accept();
            final Thread t = Thread.currentThread();
            Thread t0 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    t.interrupt();
                }
            });

            t0.start();

            byte[] data = new byte[2096];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try{
                while(i++ < 10){
                    int read = accept.getInputStream().read(data);
                    if(read != -1){
                        bos.write(data,0,read);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            byte[] bytes = bos.toByteArray();
            int len = bytes.length;
            System.out.println("len = "+len);

            System.out.println(bytes[len-1]);
            System.out.println(bytes[len-2]);
            System.out.println(bytes[len-3]);
            System.out.println(bytes[len-4]);
            System.out.println(bytes[len-5]);
            System.out.println(bytes[len-6]);

            System.out.println(new String(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMulti2(){
        try {
            Socket socket = new Socket("127.0.0.1",8080);
            String s = "POST /a.do?api=test_multi HTTP/1.1\n" +
                    "Content-Type: multipart/mixed; boundary=6d4a8f02-b7f1-4430-bffd-9af2efd0a11d\n" +
                    "Content-Length: 562\n" +
                    "Host: 127.0.0.1:8080\n" +
                    "Connection: Keep-Alive\n" +
                    "Accept-Encoding: gzip\n" +
                    "User-Agent: okhttp/3.10.0\n" +
                    "\n" +
                    "--6d4a8f02-b7f1-4430-bffd-9af2efd0a11d\n" +
                    "Content-Disposition: form-data; name=\"file1\"; filename=\"file1\"\n" +
                    "Content-Type: plain/text\n" +
                    "Content-Length: 19\n" +
                    "\n" +
                    "ksv\n" +
                    "ksv \n" +
                    "ksv\n" +
                    "ksv\n" +
                    "--6d4a8f02-b7f1-4430-bffd-9af2efd0a11d\n" +
                    "Content-Disposition: form-data; name=\"file2\"; filename=\"file2\"\n" +
                    "Content-Type: plain/text\n" +
                    "Content-Length: 19\n" +
                    "\n" +
                    "ksv\n" +
                    "ksv \n" +
                    "ksv\n" +
                    "ksv\n" +
                    "--6d4a8f02-b7f1-4430-bffd-9af2efd0a11d\n" +
                    "Content-Disposition: form-data; name=\"file3\"; filename=\"file13\"\n" +
                    "Content-Type: plain/text\n" +
                    "Content-Length: 19\n" +
                    "\n" +
                    "ksv\n" +
                    "ksv \n" +
                    "ksv\n" +
                    "ksv\n" +
                    "--6d4a8f02-b7f1-4430-bffd-9af2efd0a11d--\r\n";
            socket.getOutputStream().write(s.getBytes());
            socket.getOutputStream().flush();

            InputStream is = socket.getInputStream();
            byte[] data = new byte[6630];

            int read = is.read(data);
            System.out.println(new String(data,0,read));

            read = is.read(data);
            System.out.println(new String(data,0,read));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ioFile(){
        Client client = new Client();
        IO buffer = IOs.buffer(client);

        try {
            FileInputStream fis=  new FileInputStream("C:\\Users\\wsdchigh\\Desktop\\img1\\a1.png");
            System.out.println("origin length = "+fis.available());
            while((fis.available() > 0)){
                buffer.source(fis);
            }
            System.out.println(buffer.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBreak(){
        Client client = new Client();

        try{
            Request0.Builder builder = new Request0.Builder();
            Request0 request = builder.url("http://127.0.0.1:8080/ksv.txt")
                    .header("Range", "bytes=7-")
                    //.method("GET")
                    .build();

            client.call(request, new ICall.ICallback() {
                @Override
                public boolean success(ICall call) throws IOException {
                    System.out.println(call.response().code);
                    Map<String, String> headers = call.response().headers();
                    Set<String> set = headers.keySet();
                    for (String s : set) {
                        System.out.println("key = "+s+"     value = "+headers.get(s));
                    }
                    System.out.println(call.response().string());
                    return false;
                }

                @Override
                public void failure(ICall call, Exception e) throws IOException {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
