package com.skyguard.zmq.queue;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.LongAccumulator;

public class IncidentMessageQueue<T> {

    private LinkedBlockingDeque<T> blockingDeque = new LinkedBlockingDeque<>();

    private LongAccumulator offset = new LongAccumulator((x,y)->x+y,0L);

    public LinkedBlockingDeque<T> getBlockingDeque() {
        return blockingDeque;
    }

    public long getOffset(){
        return offset.get();
    }

    public void putData(T data) throws Exception{
        blockingDeque.put(data);
    }

    public T getData() throws Exception{
            T data  = blockingDeque.take();
            offset.accumulate(1);
            return data;
    }


}
