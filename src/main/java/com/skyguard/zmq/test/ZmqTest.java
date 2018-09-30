package com.skyguard.zmq.test;

import com.skyguard.zmq.consumer.ZmqConsumer;
import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.entity.RequestType;
import com.skyguard.zmq.processor.ZmqServerProcessor;
import com.skyguard.zmq.producer.ZmqProducer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZmqTest {

    private final Logger LOG = LoggerFactory.getLogger(ZmqTest.class);

    @Test
    public void test1(){



        ZmqServerProcessor processor = new ZmqServerProcessor(10012,20000);
        processor.startServer();






        try {
            System.in.read();
        } catch (IOException e) {

        }


    }







}
