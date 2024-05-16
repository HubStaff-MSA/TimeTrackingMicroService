package com.hubstaffmicroservices.tracktime.MQ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RabbitMQController {

    private final RabbitMQService rabbitMQService;

    @Autowired
    public RabbitMQController(RabbitMQService rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }

    @PostMapping("/send/{queueName}")
    public String sendMessage(@PathVariable String queueName, @RequestBody String message) {
        rabbitMQService.sendMessage(queueName, message);
        return "Message sent to queue: " + queueName;
    }
}
