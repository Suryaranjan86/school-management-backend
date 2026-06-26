package com.srs.school.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAcademicRecord {

    private String id;
    private String name;
    private String academicYear;
    private String clsId;
    private String studentId;
    private String classRollNo;
    private String isCurrentAcademicYear;
}

