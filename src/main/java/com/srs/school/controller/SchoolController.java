package com.srs.school.controller;

import com.srs.school.context.SchoolContext;
import com.srs.school.entity.School;
import com.srs.school.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @PostMapping
    public ResponseEntity<School> addSchool(@RequestBody School school) {
        // Use school_id from header
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId != null && !schoolId.isEmpty()) {
            school.setId(schoolId);
        }
        School savedSchool = schoolService.saveSchool(school);
        return ResponseEntity.ok(savedSchool);
    }

    @GetMapping
    public ResponseEntity<List<School>> getAllSchools() {
        List<School> schools = schoolService.getAllSchools();
        return ResponseEntity.ok(schools);
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<School> getSchoolById(@PathVariable String  schoolId) {
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
        School school = schoolService.getCurrentSchool();
        if (school != null) {
            return ResponseEntity.ok(school);
        }
        return ResponseEntity.status(401).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable String id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<School> updateSchool(@PathVariable String id, @RequestBody School school) {
        School updated = schoolService.updateSchool(id, school);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
}
