package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;

import java.util.List;

public class ListOfTimeTracksCommand implements Command{

    private int userID;

    private List<TrackTime> returned;

    public final TrackTimeService trackTimeService;

    public ListOfTimeTracksCommand(TrackTimeService trackTimeService) {
        this.trackTimeService = trackTimeService;
    }

    @Override
    public void execute() {
        this.returned = trackTimeService.getListOfTrackTimeByUserId(userID);
    }

    public void build(Object payload) {
        this.userID = Integer.parseInt((String)payload);
    }


    // int userID
    // List<TrackTime> returned;


    //excute()

    //build(payload)
}
