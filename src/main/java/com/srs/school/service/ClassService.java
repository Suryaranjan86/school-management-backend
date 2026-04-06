package com.srs.school.service;

import com.srs.school.entity.Classes;
import com.srs.school.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    public Classes saveClass(Classes cls) {
        return classRepository.save(cls);
    }

    public List<Classes> getAllClasses() {
        return classRepository.findAll();
    }

    public Classes getClassById(String id) {
        Optional<Classes> cls = classRepository.findById(id);
        return cls.orElse(null);
    }

    public void deleteClass(String id) {
        classRepository.deleteById(id);
    }

    public Classes updateClass(String id, Classes cls) {
        Optional<Classes> existing = classRepository.findById(id);
        if (existing.isPresent()) {
            cls.setId(id);
            return classRepository.save(cls);
        }
        return null;
    }
}
