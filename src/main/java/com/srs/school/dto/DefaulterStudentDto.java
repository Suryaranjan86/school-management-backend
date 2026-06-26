package com.srs.school.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaulterStudentDto {

    private String studentId;

    private String classRollNo;

    private String className;

    private String academicYear;

    private String studentName;

    private int expectedInstallments;

    private List<String> paidInstallments;

    private List<String> pendingInstallments;
}