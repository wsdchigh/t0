package com.wsdc.g_a_0;

import com.wsdc.g_a_0.chain.IChainImpl;
import com.wsdc.g_a_0.chain.Task0;
import com.wsdc.g_a_0.chain.TaskProxy;

import org.junit.Test;

import java.util.Map;

public class TaskTest {
    @Test
    public void testThen() {
        IChainImpl chain = new IChainImpl();

        chain.doMain0(new Task0.Builder().taskKey(1)
                .proxy(new TaskProxy<Map<Integer, Object>>() {
                    @Override
                    public int run(Map<Integer, Object> integerObjectMap) throws Exception {
                        System.out.println("1111111");
                        Thread.currentThread().sleep(3000);
                        return 1;
                    }
                }).build())
                .doThen(new Task0.Builder()
                        .taskKey(2)
                        .thenKey(1)
                        .registerRtnKey(1)
                        .proxy(new TaskProxy<Map<Integer, Object>>() {
                            @Override
                            public int run(Map<Integer, Object> integerObjectMap) throws Exception {
                                System.out.println("2222222");
                                Thread.currentThread().sleep(3000);
                                return 1;
                            }
                        }).build())
                .doThen(new Task0.Builder()
                        .taskKey(3)
                        .thenKey(2)
                        .registerRtnKey(1)
                        .proxy(new TaskProxy<Map<Integer, Object>>() {
                            @Override
                            public int run(Map<Integer, Object> integerObjectMap) throws Exception {
                                System.out.println("333333");
                                return 0;
                            }
                        }).build());

        Object lock = new Object();

        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testUnion() {
        IChainImpl chain = new IChainImpl();

        chain.doMain0(new Task0.Builder().taskKey(1)
                .proxy(new TaskProxy<Map<Integer, Object>>() {
                    @Override
                    public int run(Map<Integer, Object> integerObjectMap) throws Exception {
                        System.out.println("1111111");
                        Thread.currentThread().sleep(3000);
                        return 1;
                    }
                }).build())
                .doThen(new Task0.Builder()
                        .taskKey(2)
                        .thenKey(1)
                        .registerRtnKey(1)
                        .proxy(new TaskProxy<Map<Integer, Object>>() {
                            @Override
                            public int run(Map<Integer, Object> integerObjectMap) throws Exception {
                                System.out.println("2222222");
                                Thread.currentThread().sleep(3000);
                                return 1;
                            }
                        }).build())
                .doThen(new Task0.Builder()
                        .taskKey(3)
                        .unionKey(1,2)
                        .registerRtnKey(1)
                        .proxy(new TaskProxy<Map<Integer, Object>>() {
                            @Override
                            public int run(Map<Integer, Object> integerObjectMap) throws Exception {
                                System.out.println("333333");
                                return 0;
                            }
                        }).build());

        Object lock = new Object();

        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testOr() {
        IChainImpl chain = new IChainImpl();

        chain.doMain0(new Task0.Builder().taskKey(1)
                .proxy(new TaskProxy<Map<Integer, Object>>() {
                    @Override
                    public int run(Map<Integer, Object> integerObjectMap) throws Exception {
                        System.out.println("1111111");
                        Thread.currentThread().sleep(3000);
                        return 1;
                    }
                }).build())
                .doThen(new Task0.Builder()
                        .taskKey(2)
                        .thenKey(1)
                        .registerRtnKey(1)
                        .proxy(new TaskProxy<Map<Integer, Object>>() {
                            @Override
                            public int run(Map<Integer, Object> integerObjectMap) throws Exception {
                                System.out.println("2222222");
                                Thread.currentThread().sleep(3000);
                                return 1;
                            }
                        }).build())
                .doThen(new Task0.Builder()
                        .taskKey(3)
                        .orKey(1,2)
                        .registerRtnKey(1)
                        .proxy(new TaskProxy<Map<Integer, Object>>() {
                            @Override
                            public int run(Map<Integer, Object> integerObjectMap) throws Exception {
                                System.out.println("333333");
                                return 0;
                            }
                        }).build());

        Object lock = new Object();

        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
