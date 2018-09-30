package com.skyguard.zmq.netty.task;

import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.entity.RequestType;
import com.skyguard.zmq.entity.ResponseEntity;
import com.skyguard.zmq.entity.ResponseStatus;
import com.skyguard.zmq.log.ZmqLogHandler;
import com.skyguard.zmq.netty.client.CommonTcpClient;
import com.skyguard.zmq.netty.client.ZmqCommonClient;
import com.skyguard.zmq.processor.ClientMessageProcessor;
import com.skyguard.zmq.processor.ZmqClientManager;
import com.skyguard.zmq.queue.IncidentMessageQueueConsumer;
import com.skyguard.zmq.queue.IncidentMessageQueueProducer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ZmqServerTask implements Callable<ResponseEntity>{

    private RequestEntity requestEntity;

    private IncidentMessageQueueProducer producer = new IncidentMessageQueueProducer();

    private IncidentMessageQueueConsumer consumer = new IncidentMessageQueueConsumer();

    private ZmqClientManager manager;

    public ZmqServerTask(RequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    private ZmqClientManager getClientManager(){
        if(manager==null){
            manager = new ZmqClientManager(new CommonTcpClient());
        }
        return manager;
    }

    @Override
    public ResponseEntity call() throws Exception {
        ResponseEntity responseEntity = new ResponseEntity();
        try {
            if (requestEntity.getType() == RequestType.PRODUCER.getType()) {
                producer.produce(requestEntity.getTopic(), requestEntity.getData());
                sendNotification(requestEntity.getTopic());
                ZmqLogHandler.insertLog(requestEntity,requestEntity.getData());
            } else if (requestEntity.getType() == RequestType.CONSUMER.getType()) {
                Object data = consumer.consume(requestEntity.getTopic());
                responseEntity.setData(data);
                ClientMessageProcessor.registerClient(requestEntity.getTopic(),requestEntity.getIp(),requestEntity.getPort());
                ZmqLogHandler.insertLog(requestEntity,data);
            }
            responseEntity.setRequestId(requestEntity.getRequestId());
            responseEntity.setResponseCode(ResponseStatus.SUCCESS.getCode());
        }catch(Exception e){
            responseEntity.setResponseCode(ResponseStatus.ERROR.getCode());
        }
        return responseEntity;
    }

    private void sendNotification(String topic) throws Exception{

        List<InetSocketAddress> inetSocketAddressList = ClientMessageProcessor.getClient(topic);
        ZmqClientManager manager = getClientManager();
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setTopic(topic);
        requestEntity.setType(RequestType.NOTIFICATION.getType());
        requestEntity.setRequestId(UUID.randomUUID().toString());
        for(InetSocketAddress socketAddress:inetSocketAddressList){
            manager.sendMessage(socketAddress.getHostName(),socketAddress.getPort(),requestEntity);
        }


    }





}
