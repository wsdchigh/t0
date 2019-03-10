package test.wsdc.p_j_9;

import com.wsdc.p_j_0.http.Client;
import com.wsdc.p_j_0.http.io.IO;
import com.wsdc.p_j_0.http.io.IOs;

import org.junit.Test;

import java.io.IOException;

/*
 *  IO类的测试
 */
public class IOTest {
    @Test
    public void t0(){
        Client client = new Client();
        IO buffer = IOs.buffer(client);

        IO cache = IOs.buffer(client);

        try{
            buffer.source("123456789\r\n".getBytes());
            buffer.source("我爱我家11\r\n".getBytes());
            buffer.source("小楼昨夜又东风\r\n".getBytes());
            buffer.source("\r\n".getBytes());

            //System.out.println(buffer.string());

            buffer.readLine(cache);
            System.out.println(cache.string());
            buffer.readLine(cache);
            System.out.println(cache.string());

            buffer.readLine(cache);
            System.out.println(cache.string());

            buffer.readLine(cache);
            System.out.println(cache.string());

            buffer.source("11中美两开花11\r\n".getBytes());
            buffer.source("11中美两开花11\r\n".getBytes());
            buffer.source("11中美两开花11\r\n".getBytes());

            buffer.readLine(cache);
            System.out.println(cache.string());

            buffer.readLine(cache);
            System.out.println(cache.string());

            buffer.readLine(cache);
            System.out.println(cache.string());


        } catch (IOException e) {
            e.printStackTrace();
        }

        buffer.finish();
        cache.finish();
    }
}
