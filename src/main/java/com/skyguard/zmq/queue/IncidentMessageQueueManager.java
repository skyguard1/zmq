package com.skyguard.zmq.queue;

import java.util.concurrent.LinkedBlockingDeque;

public class IncidentMessageQueueManager<T> {

    private IncidentMessageQueueFactory<T> factory = new IncidentMessageQueueFactory<>();

    private static class IncidentMessageQueueManagerHolder{
        private static IncidentMessageQueueManager messageQueueManager = new IncidentMessageQueueManager();
    }

    public static IncidentMessageQueueManager getInstance(){
        return IncidentMessageQueueManagerHolder.messageQueueManager;
    }

    private IncidentMessageQueueManager(){

    }

    public IncidentMessageQueueFactory getFactory(){
        return factory;
    }

    public LinkedBlockingDeque getMessageQueue(String topic){
        return factory.getMessageQueue(topic).getBlockingDeque();
    }

    public long getOffset(String topic){
        return factory.getMessageQueue(topic).getOffset();
    }




}
