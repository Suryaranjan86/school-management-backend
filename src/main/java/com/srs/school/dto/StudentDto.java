package com.srs.school.dto;

import lombok.Data;

import java.util.Date;


@Data
public class StudentDto {
    private String name;
    private String fatherName;
    private String motherName;
    private Date dateOfBirth;
    private String address;
    private String email;
    private String batch;
    private String cls_id;
    private String school_id;
}
