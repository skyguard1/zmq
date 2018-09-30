package com.skyguard.zmq.consumer;

import com.skyguard.zmq.config.ZmqConfig;
import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.entity.ResponseEntity;
import com.skyguard.zmq.netty.client.CommonTcpClient;
import com.skyguard.zmq.processor.ZmqClientManager;
import com.skyguard.zmq.processor.ZmqRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZmqConsumer {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ZmqClientManager clientManager;

    public ZmqConsumer() {
        clientManager = new ZmqClientManager(new CommonTcpClient());
    }

    public ZmqConsumer(int port, int connectTimeout) {
        this();
        this.port = port;
        this.connectTimeout = connectTimeout;
    }

    int port;
    int connectTimeout;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void start(){
        if(port==0){
            port = Integer.parseInt(ZmqConfig.getValue("client.connect.port"));
            connectTimeout = Integer.parseInt(ZmqConfig.getValue("client.connect.timeout"));
        }
        clientManager.startClient(port,connectTimeout);
    }

    public Object consume(RequestEntity requestEntity) throws Exception{

        String serverIp = ZmqConfig.getValue("server.connect.ip");
        int serverPort = Integer.parseInt(ZmqConfig.getValue("server.connect.port"));
        clientManager.sendMessage(serverIp,serverPort,requestEntity);
        Object result = null;
        boolean flag = true;
        while (flag){
            result = ZmqRequestHandler.getData(requestEntity.getRequestId());
            if(result!=null){
                flag = false;
            }
        }

        ResponseEntity responseEntity = (ResponseEntity) result;

        return responseEntity.getData();
    }

    public void stop(){
        clientManager.stop();
    }



}
