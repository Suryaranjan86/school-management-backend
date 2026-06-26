package com.srs.school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestDto {
    private String studentId;
    private String name;
    private String fatherName;
    private String motherName;
    private Date dateOfBirth;
    private String address;
    private String email;
    private String academicYear;
    private String gender;
    private String clsId;
    private String clsName;
    private String classRollNo;
    private String isCurrentAcademicYear;
}
