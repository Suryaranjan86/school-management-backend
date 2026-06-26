package com.srs.school.controller;

import com.srs.school.dto.StudentRequestDto;
import com.srs.school.dto.StudentResponse;
import com.srs.school.service.StudentService;
import com.srs.school.dto.ClassCountDto;
import com.srs.school.dto.GenderSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> addStudent(@RequestBody StudentRequestDto student) {
        StudentResponse savedStudent = studentService.saveStudent(student);
        return ResponseEntity.ok(savedStudent);
    }

    @GetMapping
    public ResponseEntity<List<StudentRequestDto>> getAllStudents() {
        List<StudentRequestDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentRequestDto> getStudentById(@PathVariable String id) {
        StudentRequestDto student = studentService.getStudentById(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable String id, @RequestBody StudentRequestDto studentDto) {
        StudentResponse updated = studentService.updateStudent(id, studentDto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-class")
    public ResponseEntity<List<ClassCountDto>> studentsByClass() {
        List<ClassCountDto> list = studentService.getStudentsByClassSummary();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/gender-summary")
    public ResponseEntity<GenderSummaryDto> genderSummary() {
        GenderSummaryDto dto = studentService.getGenderSummary();
        return ResponseEntity.ok(dto);
    }
}
