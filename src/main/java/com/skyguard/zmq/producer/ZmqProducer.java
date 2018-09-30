package com.skyguard.zmq.producer;

import com.skyguard.zmq.config.ZmqConfig;
import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.netty.client.CommonTcpClient;
import com.skyguard.zmq.netty.client.ZmqCommonClient;
import com.skyguard.zmq.processor.ZmqClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZmqProducer {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ZmqClientManager clientManager;

    public ZmqProducer() {
        clientManager = new ZmqClientManager(new CommonTcpClient());
    }

    public ZmqProducer(int port, int connectTimeout) {
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

    public void produce(RequestEntity requestEntity) throws Exception{

        String severIp = ZmqConfig.getValue("server.connect.ip");
        int serverPort = Integer.parseInt(ZmqConfig.getValue("server.connect.port"));
        clientManager.sendMessage(severIp,serverPort,requestEntity);

    }

    public void stop(){
        clientManager.stop();
    }



}
