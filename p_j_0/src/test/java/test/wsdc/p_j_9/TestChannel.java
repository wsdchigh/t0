package test.wsdc.p_j_9;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TestChannel {
    @Test
    public void t0(){
        try {
            SocketChannel channel = SocketChannel.open();
            //channel.configureBlocking(false);
            boolean connect = channel.connect(new InetSocketAddress("img.wsdchigh.site", 80));

            System.out.println(connect);
            ByteBuffer byteBuffer = ByteBuffer.allocate(128);
            int read = channel.read(byteBuffer);
            if(read > 0){
                byte[] array = byteBuffer.array();
                System.out.println(new String(array,0,read));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
