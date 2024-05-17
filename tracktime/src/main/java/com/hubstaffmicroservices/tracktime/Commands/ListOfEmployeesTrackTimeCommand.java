package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ListOfEmployeesTrackTimeCommand  implements Command{

    private List<Integer> userIds;

    private Map<String, Map<LocalDate, Duration>>  returned;

    private final TrackTimeService trackTimeService;


    public ListOfEmployeesTrackTimeCommand(TrackTimeService trackTimeService) {
        this.trackTimeService = trackTimeService;
    }


    @Override
    public void execute() {
        this.returned = trackTimeService.getTotalDurationByUserIdsAndDays(userIds);
    }

    public void build(Object payload) {
        this.userIds = (List<Integer>) payload;
    }
}
