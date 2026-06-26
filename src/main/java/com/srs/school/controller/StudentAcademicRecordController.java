package com.srs.school.controller;

import com.srs.school.dto.ClassInfoDto;
import com.srs.school.service.StudentAcademicRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/academic")
public class StudentAcademicRecordController {

    @Autowired
    private StudentAcademicRecordService studentAcademicRecordService;

    /**
     * Get all unique academic years for a specific school
     */
    @GetMapping("/academic-years")
    public ResponseEntity<List<String>> getAllUniqueAcademicYearsBySchool(@RequestParam(required = false) String studentId) {
        List<String> years = studentAcademicRecordService.getAllUniqueAcademicYearsBySchool(studentId);
        return ResponseEntity.ok(years);
    }

    /**
     * Get all unique classes for a specific school
     */
    @GetMapping("/classes")
    public ResponseEntity<List<ClassInfoDto>> getAllUniqueClassesBySchool(@RequestParam(required = false) String studentId) {
        List<ClassInfoDto> classes = studentAcademicRecordService.getAllUniqueClassesBySchool(studentId);
        return ResponseEntity.ok(classes);
    }
}

