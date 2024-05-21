package com.hubstaffmicroservices.tracktime.MQ.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class TrackTimeDTO implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;


    private Integer userId;

    private LocalDateTime startTime;

    @Column(
            insertable = false,
            updatable = true
    )
    private LocalDateTime endTime;

}
