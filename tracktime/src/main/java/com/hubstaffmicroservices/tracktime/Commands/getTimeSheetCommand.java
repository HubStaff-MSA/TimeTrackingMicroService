package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(force = true)
public class getTimeSheetCommand implements Command {
    private int organizationID;

    private int userID;

    private TrackTime returned;

    private final TrackTimeService trackTimeService;
    @Override
    public void excute() {
        this.returned = trackTimeService.getTimeSheet(organizationID, userID);
    }


    public void build(String payload) {
        this.organizationID = Integer.parseInt(payload.split(",")[0]);
        this.userID = Integer.parseInt(payload.split(",")[1]);
    }
}
