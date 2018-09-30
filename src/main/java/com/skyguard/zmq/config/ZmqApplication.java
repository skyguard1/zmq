package com.skyguard.zmq.config;

import com.skyguard.zmq.processor.ZmqServerProcessor;

import java.io.IOException;
import java.net.URL;

public class ZmqApplication {



    public static void main(String[] args){

        ZmqServerProcessor processor = new ZmqServerProcessor();
        processor.startServer();

        




        try {
            System.in.read();
        } catch (IOException e) {

        }





    }





}
