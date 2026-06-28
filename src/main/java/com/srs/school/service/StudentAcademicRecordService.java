package com.srs.school.service;

import com.srs.school.context.SchoolContext;
import com.srs.school.dto.ClassInfoDto;
import com.srs.school.dto.StudentAcademicRecord;
import com.srs.school.repository.StudentAcademicRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class StudentAcademicRecordService {

    @Autowired
    private StudentAcademicRecordRepository studentAcademicRecordRepository;

    private static final Logger log = LoggerFactory.getLogger(StudentAcademicRecordService.class);


    /**
     * Get all unique academic years for a specific school
     */
    public List<String> getAllUniqueAcademicYearsBySchool(String studentId) {
        log.info("Entering getAllUniqueAcademicYearsBySchool - studentId: {}", studentId);
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            throw new RuntimeException("School ID is required");
        }
        return studentAcademicRecordRepository.findAllUniqueAcademicYearsBySchool(schoolId,studentId);
    }

    /**
     * Get all unique classes for a specific school
     */
    public List<ClassInfoDto> getAllUniqueClassesBySchool(String studentId) {
        log.info("Entering getAllUniqueClassesBySchool - studentId: {}", studentId);
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            throw new RuntimeException("School ID is required");
        }
        return studentAcademicRecordRepository.findAllUniqueClassesBySchool(schoolId,studentId);
    }

}

