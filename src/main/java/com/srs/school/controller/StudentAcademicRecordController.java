package com.srs.school.controller;

import com.srs.school.dto.ClassInfoDto;
import com.srs.school.service.StudentAcademicRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/students/academic")
public class StudentAcademicRecordController {

    private static final Logger log = LoggerFactory.getLogger(StudentAcademicRecordController.class);

    @Autowired
    private StudentAcademicRecordService studentAcademicRecordService;

    /**
     * Get all unique academic years for a specific school
     */
    @GetMapping("/academic-years")
    public ResponseEntity<List<String>> getAllUniqueAcademicYearsBySchool(@RequestParam(required = false) String studentId) {
        log.info("Entering getAllUniqueAcademicYearsBySchool - studentId: {}", studentId);
        List<String> years = studentAcademicRecordService.getAllUniqueAcademicYearsBySchool(studentId);
        return ResponseEntity.ok(years);
    }

    /**
     * Get all unique classes for a specific school
     */
    @GetMapping("/classes")
    public ResponseEntity<List<ClassInfoDto>> getAllUniqueClassesBySchool(@RequestParam(required = false) String studentId) {
        log.info("Entering getAllUniqueClassesBySchool - studentId: {}", studentId);
        List<ClassInfoDto> classes = studentAcademicRecordService.getAllUniqueClassesBySchool(studentId);
        return ResponseEntity.ok(classes);
    }
}

