package com.wsdc.g_a_0;

import com.wsdc.g_a_0.http.IByteData;
import com.wsdc.g_a_0.http.IClient;
import com.wsdc.g_a_0.http.impl.ByteDataImpl;
import com.wsdc.g_a_0.http.impl.IClientImpl;

import org.junit.Test;

import java.io.IOException;

/*
 *  测试  byteData的稳定性
 */
public class TestByte {
    @Test
    public void t0(){
        IClient client = new IClientImpl();
        IByteData bd = new ByteDataImpl(client,new ByteDataImpl(client,null));

        try {
            bd.write("我不是小dfwe学生1\n".getBytes());
            bd.write("我不是小dfweewfew学生2\n".getBytes());
            bd.write("我不是小df学生3\n".getBytes());
            bd.write("\n".getBytes());
            bd.write("我不是小wwwwwwwwwwww学生4\n".getBytes());
            bd.write("我不是小fffffffffff学生5\n".getBytes());
            bd.write("我不是小11111111111学生6\n".getBytes());
            bd.write("dfhuiewhehwehwiheujj\n".getBytes());
            //byte[] bytes = bd.bytes();
            //System.out.println(new String(bytes));

            System.out.println(bd.readLine());
            System.out.println(bd.readLine());
            System.out.println(bd.readLine());
            System.out.println(bd.readLine());
            System.out.println(bd.readLine());
            System.out.println(bd.readLine());
            System.out.println(bd.readLine());
            System.out.println(bd.readLine());
            System.out.println(bd.readLine());
            System.out.println(bd.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
