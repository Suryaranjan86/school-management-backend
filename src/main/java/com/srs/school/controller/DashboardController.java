package com.srs.school.controller;

import com.srs.school.dto.ClassStudentCountDto;
import com.srs.school.dto.SchoolStudentCountDto;
import com.srs.school.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/school-student-count")
    public ResponseEntity<SchoolStudentCountDto> getSchoolWiseStudentCount() {
        return ResponseEntity.ok(dashboardService.getSchoolWiseStudentCount());
    }

    @GetMapping("/class-student-count")
    public ResponseEntity<List<ClassStudentCountDto>> getClassWiseCurrentAcademicSessionStudentCount() {
        return ResponseEntity.ok(dashboardService.getClassWiseCurrentAcademicSessionStudentCount());
    }
}
