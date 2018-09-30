package com.skyguard.zmq.netty.server;

import com.skyguard.zmq.entity.RequestEntity;

public interface ZmqServer {

    public void registerProcessor(String name,Object serviceInstance);

    public void start(int port,int timeout) throws Exception;

    public void stop();

}
