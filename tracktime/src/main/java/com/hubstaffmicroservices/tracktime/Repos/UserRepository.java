package com.hubstaffmicroservices.tracktime.Repos;
import com.hubstaffmicroservices.tracktime.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

