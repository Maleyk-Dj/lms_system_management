package com.lms.lms_system_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table (name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column (name = "last_name", nullable = false, length = 50)
    private String lastName;

    @OneToMany (mappedBy = "teacher")
    private List <Course> courses;
}
