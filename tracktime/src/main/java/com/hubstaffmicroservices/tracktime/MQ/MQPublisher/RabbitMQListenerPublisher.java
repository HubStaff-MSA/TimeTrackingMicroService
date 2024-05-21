package com.hubstaffmicroservices.tracktime.MQ.MQPublisher;

import com.hubstaffmicroservices.tracktime.MQ.dto.TrackTimeDTO;
import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListenerPublisher {


//    @RabbitListener(queues = "trackTimeQueue")
//    public void receiveMessage(TrackTime trackTime) {
//        System.out.println("Received TrackTime message:");
//        System.out.println("ID: " + trackTime.getId());
//        System.out.println("User ID: " + trackTime.getUserId());
//        System.out.println("Start Time: " + trackTime.getStartTime());
//        System.out.println("End Time: " + trackTime.getEndTime());
//    }


}
