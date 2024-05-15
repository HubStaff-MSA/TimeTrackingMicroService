package com.hubstaffmicroservices.tracktime.Repos;

import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackTimeRepository extends JpaRepository<TrackTime, Integer> {
    TrackTime findFirstByOrderByStartTimeDesc();

//    List<TrackTime> findAllBy
    TrackTime findByUserId(int i);
     TrackTime findByIdAndOrganizationID(Integer id, int organizationID);
}
