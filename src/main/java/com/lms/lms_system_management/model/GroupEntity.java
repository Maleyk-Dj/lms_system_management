package com.lms.lms_system_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 100)
    private String name;

}
