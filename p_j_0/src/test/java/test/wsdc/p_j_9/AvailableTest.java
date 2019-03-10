package test.wsdc.p_j_9;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AvailableTest {
    /*
     *
     */

    @Test
    public void client(){
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
            SSLSocketFactory factory = context.getSocketFactory();
            Socket socket = factory.createSocket("127.0.0.1",8888);

            while(true){
                Thread.currentThread().sleep(1000);

                //socket.getInputStream().read();
                ((SSLSocket) socket).getSession();
                System.out.println(socket.getInputStream().available());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void server() throws KeyManagementException {
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
            ServerSocket ss = context.getServerSocketFactory().createServerSocket(8888);

            Socket socket = ss.accept();
            while(true){
                Thread.currentThread().sleep(880);
                socket.getOutputStream().write("1234567890".getBytes());
                socket.getOutputStream().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }
}
