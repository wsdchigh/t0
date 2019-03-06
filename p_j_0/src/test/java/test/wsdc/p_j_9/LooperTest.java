package test.wsdc.p_j_9;

import com.wsdc.p_j_0.looper.LCall;
import com.wsdc.p_j_0.looper.Looper;
import com.wsdc.p_j_0.looper.LooperImpl;

import org.junit.Test;

public class LooperTest {
    @Test
    public void t0(){
        Looper looper = LooperImpl.getDefault();

        for (int i = 0; i < 10; i++) {
            final int a= i;
            LCall lc = new LCall() {
                @Override
                public void toQueue() throws Exception {

                }

                @Override
                public boolean loop() throws Exception {
                    System.out.println("当前call = "+a);
                    return false;
                }

                @Override
                public void exception(int status, Exception e) {

                }

                @Override
                public void requestExit() {

                }

                @Override
                public void exitQueue() throws Exception {

                }
            };

            looper.register(lc);
        }

        try {
            Thread.currentThread().sleep(10000);
            looper.exit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
