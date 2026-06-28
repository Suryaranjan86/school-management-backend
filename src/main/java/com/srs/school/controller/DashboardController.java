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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/school-student-count")
    public ResponseEntity<SchoolStudentCountDto> getSchoolWiseStudentCount() {
        log.info("Entering getSchoolWiseStudentCount");
        return ResponseEntity.ok(dashboardService.getSchoolWiseStudentCount());
    }

    @GetMapping("/class-student-count")
    public ResponseEntity<List<ClassStudentCountDto>> getClassWiseCurrentAcademicSessionStudentCount() {
        log.info("Entering getClassWiseCurrentAcademicSessionStudentCount");
        return ResponseEntity.ok(dashboardService.getClassWiseCurrentAcademicSessionStudentCount());
    }
}
