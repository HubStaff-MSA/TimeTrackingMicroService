package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import com.hubstaffmicroservices.tracktime.dto.TT_dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListOfTimeTracksByUsersIds implements Command{

    private List<Integer> userIds;

    private Map<Integer, Long> mapOfUsersAndTime;

    private List<TT_dto> returned;

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
        this.returned = mapOfUsersAndTime.entrySet().stream()
                .map(entry -> TT_dto.builder()
                        .userId(entry.getKey())
                        .duration(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }
    public void build(Object payload) {
        this.userIds = (List<Integer>) payload;
    }
}
