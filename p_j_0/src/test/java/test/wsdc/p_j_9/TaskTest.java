package test.wsdc.p_j_9;

import com.wsdc.p_j_0.chain.IChain;
import com.wsdc.p_j_0.chain.IChainImpl;
import com.wsdc.p_j_0.chain.ITask;
import com.wsdc.p_j_0.chain.Task0;
import com.wsdc.p_j_0.chain.TaskProxy;

import org.junit.Test;

import java.util.Map;

/*
 *  关于任务的测试
 */
public class TaskTest {
    @Test
    public void testThen() {
        IChain<Integer, Map<Integer, Object>> chain = new IChainImpl();

        chain.doMain0(new Task0.Builder()
                .taskKey(1)
                .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                    @Override
                    public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                        System.out.println("11111111111111111111");

                        Thread.currentThread().sleep(3000);
                    }
                })
                .exception(new TaskProxy<Integer, Map<Integer, Object>>() {
                    @Override
                    public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                        System.out.println("11111111exception");
                    }
                })
                .build())
                .doThen(new Task0.Builder()
                        .taskKey(2)
                        .thenKey(1)
                        .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("222222222222");
                                throw new Exception("wuzhi");
                            }
                        })
                        .exception(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("222222exception");
                            }
                        })
                        .build())
                .doThen(new Task0.Builder()
                        .taskKey(3)
                        .thenKey(2)
                        .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("33333333333333");
                            }
                        })
                        .exception(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("33333333333exception");
                            }
                        })
                        .build())
                .doThen(new Task0.Builder()
                        .taskKey(4)
                        .thenKey(1)
                        .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("4444444444");
                            }
                        })
                        .exception(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("4444444444444exception");
                            }
                        })
                        .build());

        chain.start();
        try{
            Thread.currentThread().sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        chain.remove(2);
        chain.exit();
    }

    @Test
    public void testUnion(){
        IChain<Integer, Map<Integer, Object>> chain = new IChainImpl();

        chain.doMain0(new Task0.Builder()
                .taskKey(1)
                .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                    @Override
                    public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                        System.out.println("union1");
                    }
                })
                .build())
                .doThen(new Task0.Builder()
                        .taskKey(2)
                        .thenKey(1)
                        .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("union 2");

                                Thread.currentThread().sleep(3000);
                            }
                        })
                        .build())
                .doThen(new Task0.Builder()
                        .taskKey(3)
                        .unionKey(new Integer[]{1,2})
                        .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("union 3");
                            }
                        })
                        .build())
                .doThen(new Task0.Builder()
                        .taskKey(4)
                        .thenKey(1)
                        .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("union 4");
                            }
                        })
                        .build());

        chain.start();
        try{
            Thread.currentThread().sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOr(){
        IChain<Integer, Map<Integer, Object>> chain = new IChainImpl();

        chain.doMain0(new Task0.Builder()
                .taskKey(1)
                .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                    @Override
                    public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                        System.out.println("union1");
                    }
                })
                .build())
                .doThen(new Task0.Builder()
                        .taskKey(2)
                        .thenKey(1)
                        .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("union 2");

                                Thread.currentThread().sleep(3000);
                            }
                        })
                        .build())
                .doThen(new Task0.Builder()
                        .taskKey(3)
                        .orKey(new Integer[]{1,2})
                        .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("union 3");
                            }
                        })
                        .build())
                .doThen(new Task0.Builder()
                        .taskKey(4)
                        .thenKey(1)
                        .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("union 4");
                            }
                        })
                        .build())
                .doThen(new Task0.Builder()
                        .taskKey(5)
                        .thenKey(3)
                        .proxy(new TaskProxy<Integer, Map<Integer, Object>>() {
                            @Override
                            public void run(ITask<Integer, Map<Integer, Object>> task) throws Exception {
                                System.out.println("555555555555555");
                            }
                        })
                        .build());

        chain.start();
        chain.doMain0(1);
        try{
            Thread.currentThread().sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
