package com.hubstaffmicroservices.tracktime.MQ.MQConsumer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceConsumer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQServiceConsumer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

}
