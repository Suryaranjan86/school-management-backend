package com.srs.school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassStudentCountDto {
    private String className;
    private Long maleCount;
    private Long femaleCount;
    private Long totalCount;
}
