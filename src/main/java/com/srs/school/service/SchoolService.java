package com.srs.school.service;

import com.srs.school.context.SchoolContext;
import com.srs.school.entity.School;
import com.srs.school.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * Get current school from request header
     */
    public School getCurrentSchool() {
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return null;
        }
        return getSchoolById(schoolId);
    }

    public School saveSchool(School school) {
        // Use school_id from header if not provided
        if (school.getId() == null || school.getId().isEmpty()) {
            school.setId(SchoolContext.getSchoolId());
        }
        return schoolRepository.save(school);
    }

    public List<School> getAllSchools() {
        // For security, only return the school of current user
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId != null && !schoolId.isEmpty()) {
            Optional<School> school = schoolRepository.findById(schoolId);
            return school.map(List::of).orElse(List.of());
        }
        return schoolRepository.findAll();
    }

    public School getSchoolById(String id) {
        Optional<School> school = schoolRepository.findById(id);
        return school.orElse(null);
    }

    public void deleteSchool(String id) {
        schoolRepository.deleteById(id);
    }

    public School updateSchool(String id, School school) {
        Optional<School> existing = schoolRepository.findById(id);
        if (existing.isPresent()) {
            school.setId(id);
            return schoolRepository.save(school);
        }
        return null;
    }
}
