package com.srs.school.service;

import com.srs.school.context.SchoolContext;
import com.srs.school.entity.School;
import com.srs.school.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    private static final Logger log = LoggerFactory.getLogger(SchoolService.class);

    /**
     * Get current school from request header
     */
    public School getCurrentSchool() {
        log.info("Entering getCurrentSchool");
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return null;
        }
        return getSchoolById(schoolId);
    }

    public School saveSchool(School school) {
        log.info("Entering saveSchool - school: {}", school);
        // Use school_id from header if not provided
        if (school.getId() == null || school.getId().isEmpty()) {
            school.setId(SchoolContext.getSchoolId());
        }
        return schoolRepository.save(school);
    }

    public List<School> getAllSchools() {
        log.info("Entering getAllSchools");
        // For security, only return the school of current user
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId != null && !schoolId.isEmpty()) {
            Optional<School> school = schoolRepository.findById(schoolId);
            return school.map(List::of).orElse(List.of());
        }
        return schoolRepository.findAll();
    }

    public School getSchoolById(String id) {
        log.info("Entering getSchoolById - id: {}", id);
        Optional<School> school = schoolRepository.findById(id);
        return school.orElse(null);
    }

    public void deleteSchool(String id) {
        log.info("Entering deleteSchool - id: {}", id);
        schoolRepository.deleteById(id);
    }

    public School updateSchool(String id, School school) {
        log.info("Entering updateSchool - id: {}, school: {}", id, school);
        Optional<School> existing = schoolRepository.findById(id);
        if (existing.isPresent()) {
            school.setId(id);
            return schoolRepository.save(school);
        }
        return null;
    }
}
