package com.skyguard.zmq.queue;

import com.google.common.collect.Maps;

import java.util.Map;

public class IncidentMessageQueueFactory<T> {

    private Map<String,IncidentMessageQueue<T>> messageQueueMap = Maps.newConcurrentMap();

    public IncidentMessageQueue getMessageQueue(String topic){
        if(!messageQueueMap.containsKey(topic)){
            IncidentMessageQueue<T> messageQueue = new IncidentMessageQueue<>();
            messageQueueMap.put(topic,messageQueue);
        }

        return messageQueueMap.get(topic);
    }




}
