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
public abstract class ConnectionPool {
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

        if(address0 == null){
            address0 = new Address(address,port);
            address0.queue = new LinkedList<>();
            address0.capacity = client.addressCacheSize;
        }

        Connection poll = address0.queue.poll();

        if(poll == null){
            poll = new Connection(client);
        }

        poll.setCall(call);

        return poll;
    }



    //  存储地址信息
    private static class Address{
        String host;
        int port;

        //  连接host+key      根据这个key缓存address
        String key;
        int capacity = 4;

        public Address(String host, int port) {
            this.host = host;
            this.port = port;
            key = String.format(host,port);
        }

        Queue<Connection> queue = new LinkedList<>();
    }
}
