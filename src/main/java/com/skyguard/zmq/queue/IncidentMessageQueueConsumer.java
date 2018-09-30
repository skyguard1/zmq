package com.skyguard.zmq.queue;

public class IncidentMessageQueueConsumer<T> {

    public <T> T consume(String topic){

        try {
            IncidentMessageQueue<T> messageQueue = IncidentMessageQueueManager.getInstance().getFactory().getMessageQueue(topic);
            T data = messageQueue.getData();
            return data;
        }catch(Exception e){

        }

        return null;
    }


}
