package com.skyguard.zmq.processor;

import com.skyguard.zmq.config.ZmqConfig;
import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.netty.client.ZmqClient;
import com.skyguard.zmq.netty.client.ZmqClientFactory;
import com.skyguard.zmq.netty.client.ZmqCommonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ZmqClientManager {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static ZmqClient zmqClient;

    private static boolean started;



    public ZmqClientManager(ZmqClient zmqClient) {
        this.zmqClient = zmqClient;
    }

    public void startClient(int port,int connectTimeout){

        try {
            zmqClient.startClient(connectTimeout);
            started = true;
        }catch(Exception e){
            LOG.error("start client error",e);
        }

    }

    public ZmqCommonClient createClient(String ip,int port){

        int connectTimeout = Integer.parseInt(ZmqConfig.getValue("client.connect.timeout"));
        if(!started){
            startClient(port,connectTimeout);
        }

        try {
            ZmqCommonClient zmqCommonClient = zmqClient.createClient(ip, port);
            return zmqCommonClient;
        }catch(Exception e){
            LOG.error("create client error",e);
        }

        return null;
    }

    public void sendMessage(String ip,int port,RequestEntity requestEntity) throws Exception{

        String key = ip+":"+port;
        ZmqCommonClient commonClient = ZmqClientFactory.getClient(key);
        if(commonClient==null){
            commonClient = createClient(ip,port);
        }
        String uuid = UUID.randomUUID().toString();
        requestEntity.setRequestId(uuid);
        commonClient.sendRequest(requestEntity);

    }

    public static void clear(){
        started = false;
        ZmqClientFactory.clear();
    }

    public static void stop(){
        started = false;
        zmqClient.stopClient();
    }



}
