package com.srs.school.controller;

import com.srs.school.dto.StudentDto;
import com.srs.school.entity.Student;
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
    public ResponseEntity<Student> addStudent(@RequestBody StudentDto student) {
        Student savedStudent = studentService.saveStudent(student);
        return ResponseEntity.ok(savedStudent);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        Student student = studentService.getStudentById(id);
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
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody StudentDto studentDto) {
        Student updated = studentService.updateStudent(id, studentDto);
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
