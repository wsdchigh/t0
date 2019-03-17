package com.wsdc.p_j_0.http;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/*
 *  连接池
 *  <li>    缓存复用规则
 *          <li>    缓存的是空闲连接，工作中的连接会移出缓存队列
 *          <li>    同一个地址最多缓存8条连接
 *          <li>    如果缓存的连接在5分钟之内没有任何数据交互，那么将关闭这个连接
 */
public class ConnectionPool {
    String addressCombine = "%s:%d";
    Map<String,Address> addressMap = new TreeMap<>();
    Client client;

    public ConnectionPool(Client client) {
        this.client = client;
    }

    public Connection getConnection(ICall call) {
        String address = call.request().host;
        int port = call.request().port;
        String key = String.format(addressCombine,address,port);
        Address address0 = addressMap.get(key);

        //System.out.println("address == null "+(address0 == nu\\));

        if(address0 == null){
            address0 = new Address(address,port,client);
            address0.queue = new LinkedList<>();
            address0.capacity = client.addressCacheSize;
        }

        //
        addressMap.put(key,address0);

        System.out.println(address0.queue.size());

        Connection poll = address0.queue.poll();

        if(poll == null){
            poll = new Connection(client,address0);
            poll.setCall(call);
            client.connectionIThread.doLast(poll);
        }else{
            poll.setCall(call);
            client.looper.register(poll);
        }

        return poll;
    }

    /*
     *  退出
     */
    public void exit(){

    }



    //  存储地址信息
    static class Address{
        String host;
        int port;

        //  连接host+key      根据这个key缓存address
        String key;
        int capacity;

        public Address(String host, int port,Client client) {
            this.host = host;
            this.port = port;
            key = host+"_"+port;
            capacity = client.addressCacheSize;
        }

        Queue<Connection> queue = new LinkedList<>();
    }
}
