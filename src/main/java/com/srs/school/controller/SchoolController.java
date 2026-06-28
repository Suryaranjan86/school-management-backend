package com.srs.school.controller;

import com.srs.school.context.SchoolContext;
import com.srs.school.entity.School;
import com.srs.school.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    private static final Logger log = LoggerFactory.getLogger(SchoolController.class);

    @Autowired
    private SchoolService schoolService;

    @PostMapping
    public ResponseEntity<School> addSchool(@RequestBody School school) {
        // Use school_id from header
        String schoolId = SchoolContext.getSchoolId();
        log.info("Entering addSchool - header schoolId: {}, payload id: {}", schoolId, school != null ? school.getId() : null);
        if (schoolId != null && !schoolId.isEmpty()) {
            school.setId(schoolId);
        }
        School savedSchool = schoolService.saveSchool(school);
        return ResponseEntity.ok(savedSchool);
    }

    @GetMapping
    public ResponseEntity<List<School>> getAllSchools() {
        log.info("Entering getAllSchools");
        List<School> schools = schoolService.getAllSchools();
        return ResponseEntity.ok(schools);
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<School> getSchoolById(@PathVariable String  schoolId) {
        log.info("Entering getSchoolById - schoolId: {}", schoolId);
        School school = schoolService.getSchoolById(schoolId);
        if (school != null) {
            return ResponseEntity.ok(school);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get current school from header
     */
    @GetMapping("/current/info")
    public ResponseEntity<School> getCurrentSchool() {
        log.info("Entering getCurrentSchool");
        School school = schoolService.getCurrentSchool();
        if (school != null) {
            return ResponseEntity.ok(school);
        }
        return ResponseEntity.status(401).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable String id) {
        log.info("Entering deleteSchool - id: {}", id);
        schoolService.deleteSchool(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<School> updateSchool(@PathVariable String id, @RequestBody School school) {
        log.info("Entering updateSchool - id: {}, school: {}", id, school);
        School updated = schoolService.updateSchool(id, school);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
}
