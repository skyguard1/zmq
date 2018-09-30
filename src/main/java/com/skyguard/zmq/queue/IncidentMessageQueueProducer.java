package com.skyguard.zmq.queue;

public class IncidentMessageQueueProducer<T> {


    public <T> boolean produce(String topic,T data){

        boolean flag = true;

        try {
            IncidentMessageQueue<T> messageQueue = IncidentMessageQueueManager.getInstance().getFactory().getMessageQueue(topic);
            messageQueue.putData(data);
        }catch(Exception e){
            flag = false;
        }

        return flag;
    }


}
