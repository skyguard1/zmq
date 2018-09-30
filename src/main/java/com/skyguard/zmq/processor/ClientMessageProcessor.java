package com.skyguard.zmq.processor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public class ClientMessageProcessor {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

     private static Map<String,List<InetSocketAddress>> clientMap = Maps.newConcurrentMap();

    public static void registerClient(String topic,String ip,int port){


        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip,port);
        if(!clientMap.containsKey(topic)){
            List<InetSocketAddress> inetSocketAddressList = Lists.newArrayList(inetSocketAddress);
            clientMap.put(topic,inetSocketAddressList);
        }else{
            List<InetSocketAddress> inetSocketAddressList = clientMap.get(topic);
            inetSocketAddressList.add(inetSocketAddress);
        }

    }

    public static List<InetSocketAddress> getClient(String topic){

        List<InetSocketAddress> socketAddressList = Lists.newArrayList();

        if(clientMap.containsKey(topic)){
            socketAddressList = clientMap.get(topic);
        }

        return socketAddressList;
    }

    public static void removeClient(String topic,String ip,int port){

        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip,port);
        if(clientMap.containsKey(topic)){
            List<InetSocketAddress> socketAddressList = clientMap.get(topic);
            socketAddressList.remove(inetSocketAddress);
        }

    }

    public static void clear(){
        clientMap.clear();
    }


}
