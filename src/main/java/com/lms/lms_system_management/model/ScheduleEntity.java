package com.lms.lms_system_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("deleted=false")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity groupEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity courseEntity;

    @Column(nullable = false)
    private LocalDateTime dateClass;

    @Column(nullable = false)
    private Boolean deleted = false;

}
