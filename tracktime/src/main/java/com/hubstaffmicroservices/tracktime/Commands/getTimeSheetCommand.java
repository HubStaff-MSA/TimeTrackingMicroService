package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.Commands.Command;
import com.hubstaffmicroservices.tracktime.MQ.MQConsumer.RabbitMQListenerConsumer;
import com.hubstaffmicroservices.tracktime.MQ.dto.CommandSender;
import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;


@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(force = true)
public class getTimeSheetCommand  implements Command {

    private int organizationID;

    private int userID;

    private TrackTime returned;

    private final TrackTimeService trackTimeService;

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMQListenerConsumer rabbitMQListenerConsumer;

    public getTimeSheetCommand(TrackTimeService trackTimeService, RabbitTemplate rabbitTemplate, RabbitMQListenerConsumer rabbitMQListenerConsumer) {
        this.trackTimeService = trackTimeService;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQListenerConsumer = rabbitMQListenerConsumer;
    }
//    @Autowired

    @Override
    public void execute() {
        this.returned = this.trackTimeService.getTimeSheet(this.organizationID, this.userID);
//        new RabbitMQListenerConsumer(this.trackTimeService, null, "commandQueueTimeTracking")
        CommandSender commandSender = new CommandSender("getProjects" , null , "P_TT_Queue" ,"TT_P_Queue" );
        rabbitTemplate.convertAndSend(commandSender.getPublishedQueue(), commandSender);
//        rabbitMQListenerConsumer.getMyRetuned();
        if(this.organizationID == 1 && this.userID == 1){
            System.out.println("Invalid organizationID or userID");
        }
    }

    public void build(Object payload) {
        this.organizationID = Integer.parseInt(((String)payload).split(",")[0]);
        this.userID = Integer.parseInt(((String)payload).split(",")[1]);
    }
}
