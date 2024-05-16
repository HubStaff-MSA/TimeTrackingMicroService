package com.hubstaffmicroservices.tracktime.MQ;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;
    private final Map<String, Queue> queues = new HashMap<>();

    @Autowired
    public RabbitMQService(RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }


    public void addQueue(String queueName) {
        Queue queue = new Queue(queueName, false);
        queues.put(queueName, queue);
        rabbitMQConfig.rabbitAdmin((CachingConnectionFactory) rabbitTemplate.getConnectionFactory()).declareQueue(queue);
    }

    public void sendMessage(String queueName, String message) {
        if (!queues.containsKey(queueName)) {
            addQueue(queueName);
        }
        System.out.println("msg sent "+ message + " to "+ queueName);
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
