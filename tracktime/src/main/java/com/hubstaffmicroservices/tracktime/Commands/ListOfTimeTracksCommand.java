package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import com.hubstaffmicroservices.tracktime.dto.TT_dto;

import java.util.List;

public class ListOfTimeTracksCommand implements Command{

    private int userID;

    private List<TT_dto> returned;

    public final TrackTimeService trackTimeService;

    public ListOfTimeTracksCommand(TrackTimeService trackTimeService) {
        this.trackTimeService = trackTimeService;
    }

    @Override
    public void execute() {
        List<TrackTime> usedDTO= trackTimeService.getListOfTrackTimeByUserId(userID);
        this.returned = usedDTO.stream()
                .map(TrackTime -> TT_dto.builder()
                        .organizationID(TrackTime.getOrganizationID())
                        .project(TrackTime.getProject())
                        .to_do(TrackTime.getTo_do())
                        .userName(TrackTime.getUserName())
                        .startTime(TrackTime.getStartTime())
                        .endTime(TrackTime.getEndTime())
                        .day(TrackTime.getDay())
                        .duration(TrackTime.getDuration())
                        .build())
                .toList();
    }

    public void build(Object payload) {
        this.userID = Integer.parseInt((String)payload);
    }


    // int userID
    // List<TrackTime> returned;


    //excute()

    //build(payload)
}
