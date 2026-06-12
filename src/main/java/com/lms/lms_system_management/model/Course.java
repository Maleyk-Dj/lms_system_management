package com.lms.lms_system_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Course {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false, length = 100)
    private String name;
    @Column (nullable = false, length = 255)
    private String description;
    @ManyToOne (optional = false)
    @JoinColumn (name = "teacher_id", nullable = false)
    private Teacher teacher;
}
