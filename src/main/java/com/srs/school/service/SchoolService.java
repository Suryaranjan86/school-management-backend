package com.srs.school.service;

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

    public School saveSchool(School school) {
        return schoolRepository.save(school);
    }

    public List<School> getAllSchools() {
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
