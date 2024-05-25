package com.hubstaffmicroservices.tracktime.Repos;
import com.hubstaffmicroservices.tracktime.Models.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
}
