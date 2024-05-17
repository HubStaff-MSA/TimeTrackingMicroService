package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.Commands.Command;
import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    public getTimeSheetCommand(TrackTimeService trackTimeService) {
        this.trackTimeService = trackTimeService;
    }
//    @Autowired

    @Override
    public void execute() {
        this.returned = trackTimeService.getTimeSheet(organizationID, userID);
        if(this.organizationID == 1 && this.userID == 1){
            System.out.println("Invalid organizationID or userID");
        }
    }

    public void build(Object payload) {
        this.organizationID = Integer.parseInt(((String)payload).split(",")[0]);
        this.userID = Integer.parseInt(((String)payload).split(",")[1]);
    }
}
