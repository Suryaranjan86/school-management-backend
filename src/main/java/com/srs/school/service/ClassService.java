package com.srs.school.service;

import com.srs.school.entity.Classes;
import com.srs.school.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService {
    @Autowired
    private ClassRepository classRepository;

    private static final Logger log = LoggerFactory.getLogger(ClassService.class);

    public Classes saveClass(Classes cls) {
        log.info("Entering saveClass - class: {}", cls);
        return classRepository.save(cls);
    }

    public List<Classes> getAllClasses() {
        log.info("Entering getAllClasses");
        return classRepository.findAll();
    }

    public Classes getClassById(String id) {
        log.info("Entering getClassById - id: {}", id);
        Optional<Classes> cls = classRepository.findById(id);
        return cls.orElse(null);
    }

    public void deleteClass(String id) {
        log.info("Entering deleteClass - id: {}", id);
        classRepository.deleteById(id);
    }

    public Classes updateClass(String id, Classes cls) {
        log.info("Entering updateClass - id: {}, class: {}", id, cls);
        Optional<Classes> existing = classRepository.findById(id);
        if (existing.isPresent()) {
            cls.setId(id);
            return classRepository.save(cls);
        }
        return null;
    }
}
