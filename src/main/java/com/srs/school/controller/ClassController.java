package com.srs.school.controller;

import com.srs.school.entity.Classes;
import com.srs.school.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private static final Logger log = LoggerFactory.getLogger(ClassController.class);

    @Autowired
    private ClassService classService;

    @PostMapping
    public ResponseEntity<Classes> addClass(@RequestBody Classes cls) {
        log.info("Entering addClass - class: {}", cls);
        Classes savedClass = classService.saveClass(cls);
        return ResponseEntity.ok(savedClass);
    }

    @GetMapping
    public ResponseEntity<List<Classes>> getAllClasses() {
        log.info("Entering getAllClasses");
        List<Classes> classes = classService.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classes> getClassById(@PathVariable String id) {
        log.info("Entering getClassById - id: {}", id);
        Classes cls = classService.getClassById(id);
        if (cls != null) {
            return ResponseEntity.ok(cls);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable String id) {
        log.info("Entering deleteClass - id: {}", id);
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Classes> updateClass(@PathVariable String id, @RequestBody Classes cls) {
        log.info("Entering updateClass - id: {}, class: {}", id, cls);
        Classes updated = classService.updateClass(id, cls);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
}
