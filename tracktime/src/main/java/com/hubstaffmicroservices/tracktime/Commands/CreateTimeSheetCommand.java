package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.MQ.MQConsumer.RabbitMQListenerConsumer;
import com.hubstaffmicroservices.tracktime.MQ.dto.CommandSender;
import com.hubstaffmicroservices.tracktime.MQ.dto.ProjectDTO;
import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(force = true)
public class CreateTimeSheetCommand implements Command{

    private String givenTracktime;

    private TrackTime returned;

    private final TrackTimeService trackTimeService;

    private final RabbitTemplate rabbitTemplate;

//
//    private final RabbitMQListenerConsumer rabbitMQListenerConsumer;

    public CreateTimeSheetCommand(TrackTimeService trackTimeService ,RabbitTemplate rabbitTemplate) {
        this.trackTimeService = trackTimeService;
        this.rabbitTemplate = rabbitTemplate;

//        this.rabbitMQListenerConsumer = rabbitMQListenerConsumer;
    }
//    @Autowired

    @Override
    public void execute() {
        CommandSender commandSender = new CommandSender("getProjectsByUserIdCommand", "1", "P_TT_Queue", "commandQueueProjects");
        // Send message to the queue
        rabbitTemplate.convertAndSend(commandSender.getPublishedQueue(), commandSender);
        System.out.println("yes I recieved it");
        // Set up a temporary reply queue
        String replyQueue = "P_TT_Queue";
        rabbitTemplate.setReplyAddress(replyQueue);
       // rabbitTemplate.setReplyTimeout(10000);

        // Receive message from the reply queue
        Message response = rabbitTemplate.receive(replyQueue);
        System.out.println(response);

        if (response != null) {
            List<ProjectDTO> returnedProjectDTO = (List<ProjectDTO>) rabbitTemplate.getMessageConverter().fromMessage(response);

            System.out.println(returnedProjectDTO);
//            if (returnedProjectDTO.stream().anyMatch(project -> project.getProjectName().equals(givenTracktime.getProject()))) {
//                this.trackTimeService.saveTrackTimeDataBase(givenTracktime);
//                this.returned = TrackTime.builder().project("Yes We did it").build();
//            }
        } else {
            System.out.println("No response received within the timeout period.");
        }

//        new RabbitMQListenerConsumer(this.trackTimeService, null, "commandQueueTimeTracking")
//        CommandSender commandSender = new CommandSender("getProjects", null, "P_TT_Queue", "TT_P_Queue");
        // send to Project Queue
//        rabbitTemplate.convertAndSend("TT_P_Queue", commandSender);
//        this.helperMQ.SendToMQ(commandSender);
//        this.helperMQ.receiveMessage();
//        // retunr from Project Queue
//        List<ProjectDTO> returnedProjectDTO = (List<ProjectDTO>) rabbitMQListenerConsumer.getMyRetuned();
//
//        if (returnedProjectDTO.contains(givenTracktime.getProject())) {
//            this.trackTimeService.saveTrackTimeDataBase(givenTracktime);
//            this.returned = TrackTime.builder()
//                    .project("Yes We did it").build();
//        }
    }

    public void build(Object payload) {
        this.givenTracktime =  (String) payload;
//        this.organizationID = Integer.parseInt(((String)payload).split(",")[0]);
//        this.userID = Integer.parseInt(((String)payload).split(",")[1]);
    }
}
