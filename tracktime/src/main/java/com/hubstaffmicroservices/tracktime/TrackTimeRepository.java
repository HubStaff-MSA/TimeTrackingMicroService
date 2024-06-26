package com.hubstaffmicroservices.tracktime;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackTimeRepository extends JpaRepository<TrackTime, Integer> {
    TrackTime findFirstByOrderByStartTimeDesc();

    TrackTime findByUserId(int i);

}
