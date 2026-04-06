package com.srs.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(generator = "stu_id_gen")
    @GenericGenerator(name = "stu_id_gen", strategy = "com.srs.school.entity.idgenerator.StuIdGenerator")
    private String id;

    private String name;
    private String fatherName;
    private String motherName;
    private Date dateOfBirth;
    private String address;
    private String email;
    private String batch;

    @ManyToOne
    @JoinColumn(name = "cls_id")
    private Classes cls;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
}
