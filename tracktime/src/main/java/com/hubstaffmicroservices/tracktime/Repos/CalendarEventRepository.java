package com.hubstaffmicroservices.tracktime.Repos;

import com.hubstaffmicroservices.tracktime.Models.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
}

