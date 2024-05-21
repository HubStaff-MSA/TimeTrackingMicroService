package com.hubstaffmicroservices.tracktime.MQ.MQPublisher;


import com.hubstaffmicroservices.tracktime.MQ.dto.CommandSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServicePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQServicePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String queueName, CommandSender commandSender) {
        System.out.println("MSG SENT ");
        rabbitTemplate.convertAndSend(queueName, commandSender);
    }
}
