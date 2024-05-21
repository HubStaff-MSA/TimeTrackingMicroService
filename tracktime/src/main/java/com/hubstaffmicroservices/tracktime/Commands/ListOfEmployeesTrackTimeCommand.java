package com.hubstaffmicroservices.tracktime.Commands;

import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import com.hubstaffmicroservices.tracktime.dto.TT_dto;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ListOfEmployeesTrackTimeCommand  implements Command{

    private Map<String,Integer> mapOfProjectUserID;

//    private   mmap;

    private List<TT_dto> returned;

    private final TrackTimeService trackTimeService;


    public ListOfEmployeesTrackTimeCommand(TrackTimeService trackTimeService) {
        this.trackTimeService = trackTimeService;
    }


    @Override
    public void execute() {
//        this.mmap = trackTimeService.getTotalDurationByUserIdsAndDays(userIds);
//        StringBuilder builder = new StringBuilder();
//        builder.append("{\n");
//        for (Map.Entry<String, Map<LocalDate, Duration>> entry : this.mmap.entrySet()) {
//            builder.append("\t").append(entry.getKey()).append(":\n");
//            for (Map.Entry<LocalDate, Duration> innerEntry : entry.getValue().entrySet()) {
//                builder.append("\t\t").append(innerEntry.getKey()).append(": ").append(innerEntry.getValue()).append("\n");
//            }
//        }
//        builder.append("}");
//        this.returned = TT_dto.builder()
//                .mapOfUserIDProjectNameDuration(mmap)
//                .build();
//        List<TrackTime> usedDTO= trackTimeService.getTotalDurationByUserIdsAndDays(userID);
//        this.returned = usedDTO.stream()
//                .map(TrackTime -> TT_dto.builder()
//                        .organizationID(TrackTime.getOrganizationID())
//                        .project(TrackTime.getProject())
//                        .to_do(TrackTime.getTo_do())
//                        .userName(TrackTime.getUserName())
//                        .startTime(TrackTime.getStartTime())
//                        .endTime(TrackTime.getEndTime())
//                        .day(TrackTime.getDay())
//                        .duration(TrackTime.getDuration())
//                        .build())
//                .toList();

    }

    public void build(Object payload) {
        this.mapOfProjectUserID = (Map<String,Integer>) payload;
    }

}
