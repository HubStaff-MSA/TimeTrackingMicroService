package com.hubstaffmicroservices.tracktime.MQ;


import com.hubstaffmicroservices.tracktime.MQ.dto.CommandSender;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class  RabbitMQConfig {

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory cachingConnectionFactory) {
        return new RabbitAdmin(cachingConnectionFactory);
    }

    @Bean
    public Queue commandQueueTimeTrack() {
        return new Queue("commandQueueTimeTracking", false);
    }
    @Bean
    public Queue commandQueueProjects() {
        return new Queue("commandQueueProjects", false);
    }
    @Bean
    public Queue ReportsQueue() {
        return new Queue("TT_R_Queue", false);
    }
    @Bean
    public Queue FiananceQueue() {
        return new Queue("TT_F_Queue", false);
    }

    @Bean
    public Queue PMQueue() {
        return new Queue("P_TT_Queue", false);
    }




    @Bean
    public Queue project_reportsQueue()  {return new Queue("project_reportsQueue", false);}
    @Bean
    public Queue todo_reportsQueue()  {return new Queue("todo_reportsQueue", false);}
    @Bean
    public Queue client_reportsQueue()  {return new Queue("client_reportsQueue", false);}
    @Bean
    public Queue timetrack_reportsQueue()  {return new Queue("timetrack_reportsQueue", false);}
    @Bean
    public Queue user_reportsQueue()  {return new Queue("user_reportsQueue", false);}
    @Bean
    public Queue payment_reportsQueue()  {return new Queue("payment_reportsQueue", false);}


    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("commandSender", CommandSender.class);
        typeMapper.setIdClassMapping(idClassMapping);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
