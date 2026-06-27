package com.srs.school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolStudentCountDto {
    private Long totalStudents;
    private Long maleCount;
    private Long femaleCount;
}
