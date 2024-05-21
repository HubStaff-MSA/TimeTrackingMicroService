package com.hubstaffmicroservices.tracktime.MQ.MQConsumer;

import com.hubstaffmicroservices.tracktime.MQ.MQPublisher.RabbitMQServicePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RabbitMQControllerConsumer {

    private final RabbitMQServicePublisher rabbitMQService;

    @Autowired
    public RabbitMQControllerConsumer(RabbitMQServicePublisher rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }

}
