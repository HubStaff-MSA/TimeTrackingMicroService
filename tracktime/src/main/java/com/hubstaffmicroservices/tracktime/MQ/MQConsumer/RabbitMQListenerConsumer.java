package com.hubstaffmicroservices.tracktime.MQ.MQConsumer;

import com.hubstaffmicroservices.tracktime.Controller.CommandsMap;
import com.hubstaffmicroservices.tracktime.MQ.dto.CommandSender;
import com.hubstaffmicroservices.tracktime.MQ.dto.ProjectDTO;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RabbitMQListenerConsumer {
    private final TrackTimeService trackTimeService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQListenerConsumer(TrackTimeService trackTimeService, RabbitTemplate rabbitTemplate) {
        this.trackTimeService = trackTimeService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "commandQueueTimeTracking")
    public void receiveMessage(CommandSender commandSender) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        System.out.println("Received command: " + commandSender.getCommand());
        System.out.println("Received payload: " + commandSender.getPayload());
       // System.out.println("Response Queue : " + commandSender.getRequestQueue());

        Object returnedValue = callCmdMap(commandSender.getCommand() , commandSender.getPayload());

        System.out.println(returnedValue);
        // Cast the returned value to TrackTime
        if (returnedValue != null) {
            rabbitTemplate.convertAndSend(commandSender.getRequestingQueue(), returnedValue);
            System.out.println("Published returned value to tracktime queue.");
        }
    }

//    @RabbitListener(queues = "WebServerCommandQueueTimeTracking")
//    public Object WebServerReceiveMessage(CommandSender commandSender) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
//        System.out.println("Received command: " + commandSender.getCommand());
//        System.out.println("Received payload: " + commandSender.getPayload());
//        System.out.println("Response Queue : " + commandSender.getRequestQueue());
//
//        Object returnedValue = callCmdMap(commandSender.getCommand() , commandSender.getPayload());
//
//        return returnedValue;
//    }




    public Object callCmdMap(String Command , Object Payload) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException {
        Field cmdMapField = CommandsMap.class.getDeclaredField("cmdMap");

        // Make the field accessible
        cmdMapField.setAccessible(true);
        // Get the cmdMap value
        Object cmdMapValue = cmdMapField.get(null);
        // Assuming cmdMapValue is a Map<String, Class<?>>
        ConcurrentHashMap<String, Class<?>> cmdMap = (ConcurrentHashMap<String, Class<?>>) cmdMapValue;

        Class<?> commandClass = (Class<?>) cmdMap.get(Command);

        // Object commandInstance = commandClass.newInstance();
        Object commandInstance = commandClass.getDeclaredConstructor(TrackTimeService.class , RabbitTemplate.class).newInstance(trackTimeService , rabbitTemplate );

        // Get the build method of the command class
        Method buildMethod = commandClass.getDeclaredMethod("build", Object.class);

        // Invoke the build method
        buildMethod.invoke(commandInstance, Payload);

        // Get the execute method of the command class
        Method executeMethod = commandClass.getDeclaredMethod("execute", null);

        // Invoke the execute method
        executeMethod.invoke(commandInstance);

        // Get the returned field of the command class
        Field returnedField = commandClass.getDeclaredField("returned");

        // Make the field accessible
        returnedField.setAccessible(true);

        // Get the value of the returned field from the command instance
        Object returnedValue = (Object) returnedField.get(commandInstance);

        return returnedValue;
    }

//    @RabbitListener(queues = "P_TT_Queue")
//    public void receivePM(List<ProjectDTO> projectDTO) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
//        {
//                this.myRetuned  = projectDTO;
//        }
//    }

}
