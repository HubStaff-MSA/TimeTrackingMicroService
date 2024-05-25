package com.hubstaffmicroservices.tracktime.Repos;

import com.hubstaffmicroservices.tracktime.Models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}

