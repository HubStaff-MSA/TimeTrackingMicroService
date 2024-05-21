package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;

import java.util.List;
import java.util.Map;

public class ListOfTimeTracksByUsersIds implements Command{

    private List<Integer> userIds;

    private Map<Integer, Long> mapOfUsersAndTime;

    private String returned;

    private final TrackTimeService trackTimeService;

    public ListOfTimeTracksByUsersIds(TrackTimeService trackTimeService) {
        this.trackTimeService = trackTimeService;
    }

    @Override
    public void execute() {
        String result = "";
        this.mapOfUsersAndTime = trackTimeService.getTotalDurationByUserIds(userIds);
        for (Map.Entry<Integer, Long> entry : this.mapOfUsersAndTime.entrySet()) {
            result += "User ID: " + entry.getKey() + ", Duration: " + entry.getValue() + " hours\n" ;
        }
        this.returned = result;
    }
    public void build(Object payload) {
        this.userIds = (List<Integer>) payload;
    }
}
