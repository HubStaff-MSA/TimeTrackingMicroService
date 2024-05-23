package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import com.hubstaffmicroservices.tracktime.dto.TT_dto;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListOfEmployeesTrackTimeCommand  implements Command{

    private Map<Integer, List<String>> mapOfUserIdProject;
    private List<TT_dto> returned;

    private final TrackTimeService trackTimeService;


    public ListOfEmployeesTrackTimeCommand(TrackTimeService trackTimeService) {
        this.trackTimeService = trackTimeService;
    }


    @Override
    public void execute() {
        // Call the service method to get the total duration map for each project for each user ID
        Map<Integer, Map<String, Long>> totalDurationMap = trackTimeService.getTotalDurationOfEachUserAtProject(mapOfUserIdProject);

        // Map the result to a list of TT_dto objects
        this.returned = totalDurationMap.entrySet().stream()
                .flatMap(userEntry -> userEntry.getValue().entrySet().stream()
                        .map(projectEntry -> TT_dto.builder()
                                .userId(userEntry.getKey())
                                .project(projectEntry.getKey())
                                .duration(projectEntry.getValue())
                                .build()))
                .collect(Collectors.toList());
    }
    public void build(Object payload) {
        this.mapOfUserIdProject = new HashMap<>();
        Map<String, List<String>> payloadMap = (Map<String, List<String>>) payload;
        for (Map.Entry<String, List<String>> entry : payloadMap.entrySet()) {
            try {
                int userId = Integer.parseInt(entry.getKey());
                this.mapOfUserIdProject.put(userId, entry.getValue());
            } catch (NumberFormatException e) {
                System.err.println("Invalid user ID: " + entry.getKey());
                // Handle or log the exception as needed
            }
        }
    }
}
