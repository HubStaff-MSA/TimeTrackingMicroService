package com.hubstaffmicroservices.tracktime.Models;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private int scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", referencedColumnName = "shift_id", nullable = false)
    private Shift shift;

    @Column(name = "scheduled_date", nullable = false)
    private java.sql.Date scheduledDate;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    public Schedule(Shift shift, java.sql.Date scheduledDate) {
        this.shift = shift;
        this.scheduledDate = scheduledDate;
    }
}
