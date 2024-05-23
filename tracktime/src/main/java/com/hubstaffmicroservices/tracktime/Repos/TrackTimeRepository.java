package com.hubstaffmicroservices.tracktime.Repos;

import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TrackTimeRepository extends JpaRepository<TrackTime, Integer> {
    TrackTime findFirstByOrderByStartTimeDesc();

    List<TrackTime> findAllByUserId(int i);
    TrackTime findByUserId(int i);

    List<TrackTime> findByUserIdAndProject(Integer userId, String project);


    TrackTime findByUserIdAndOrganizationID(Integer id, int organizationID);

    List<TrackTime> findByUserIdIn(List<Integer> userIds);

    @Query("SELECT t.userName, t.day, SUM(t.duration) FROM TrackTime t WHERE t.userId IN :userIds GROUP BY t.userName, t.day")
    Map<String, Map<LocalDate, Duration>> getTotalDurationByUserIdsAndDays(@Param("userIds") List<Integer> userIds);



}
