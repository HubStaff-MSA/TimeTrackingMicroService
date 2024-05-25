package com.hubstaffmicroservices.tracktime.MQ.MQPublisher;

import com.hubstaffmicroservices.tracktime.MQ.dto.CommandSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;

@RestController
@RequestMapping("/api")
public class RabbitMQControllerPublisher {

    private final RabbitMQServicePublisher rabbitMQService;

    @Autowired
    public RabbitMQControllerPublisher(RabbitMQServicePublisher rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }

    @PostMapping("/sendCommandTrackTime")
    public String sendCommandProjects(@RequestBody CommandSender commandSender) {
        try {
            rabbitMQService.sendMessage("commandQueueTimeTracking", commandSender);
            return "Message sent to commandQueue: " + commandSender;
        } catch (Exception e) {
            return "Failed to send message: " + e.getMessage();
        }
    }
}
