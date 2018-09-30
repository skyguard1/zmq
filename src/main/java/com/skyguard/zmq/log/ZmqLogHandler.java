package com.skyguard.zmq.log;

import com.skyguard.zmq.config.ZmqConfig;
import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.entity.RequestType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ZmqLogHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ZmqLogHandler.class);

    public static void insertLog(RequestEntity requestEntity,Object data){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(requestEntity.getIp()).append(":").append(requestEntity.getPort())
                .append(" ");
        if(requestEntity.getType()== RequestType.PRODUCER.getType()){
            stringBuilder.append("produce data:");
        }else{
            stringBuilder.append("consume data:");
        }

        stringBuilder.append(data.toString());
        stringBuilder.append(" ");

        DateTime dateTime = DateTime.now();

        stringBuilder.append(dateTime.toString("yyyy-MM-dd HH:mm:ss"));


        if(System.getProperty("os.name").contains("Linux")){
            stringBuilder.append("\n");
        }else {
            stringBuilder.append("\r\n");
        }

        String str = stringBuilder.toString();

        String logPath = ZmqConfig.getValue("zmq.log.path");

        FileOutputStream outputStream = null;
        FileChannel channel = null;

        try {
            if (Files.notExists(Paths.get(logPath))) {
                Files.createDirectories(Paths.get(logPath));
            }

            String fileName = logPath+"/message-"+requestEntity.getTopic()+".log";

            if(Files.notExists(Paths.get(fileName))){
                Files.createFile(Paths.get(fileName));
            }

            outputStream = new FileOutputStream(fileName,true);
            channel = outputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
            channel.write(byteBuffer);


        }catch (Exception e){
            LOG.error("get file error",e);
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {

                }
                try {
                    channel.close();
                } catch (IOException e) {

                }
            }
        }


    }




}
