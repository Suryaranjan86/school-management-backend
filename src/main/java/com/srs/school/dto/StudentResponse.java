package com.srs.school.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private String id;
    private String name;
    private String email;
    private String gender;
    private String fatherName;
    private String motherName;
    private Date dateOfBirth;
    private String address;
    private String academicYear;
    private String clsId;
    private String clsName;
    private String classRollNo;
    private String isCurrentAcademicYear;
    private String schoolId;
    private String schoolName;

}
