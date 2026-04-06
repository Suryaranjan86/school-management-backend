package com.srs.school.service;

import com.srs.school.dto.StudentDto;
import com.srs.school.entity.Classes;
import com.srs.school.entity.School;
import com.srs.school.entity.Student;
import com.srs.school.repository.ClassRepository;
import com.srs.school.repository.SchoolRepository;
import com.srs.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    public Student saveStudent(StudentDto studentDto) {
        Classes cls = classRepository.findById(studentDto.getCls_id())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        School school = schoolRepository.findById(studentDto.getSchool_id())
                .orElseThrow(() -> new RuntimeException("School not found"));
        var student = Student.builder()
                .name(studentDto.getName())
                .dateOfBirth(studentDto.getDateOfBirth())
                .fatherName(studentDto.getFatherName())
                .motherName(studentDto.getMotherName())
                .address(studentDto.getAddress())
                .email(studentDto.getEmail())
                .batch(studentDto.getBatch())
                .cls(cls)
                .school(school)
                .build();
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

     public Student getStudentById(String id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    public void deleteStudent(String id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(String id, StudentDto studentDto) {
        Optional<Student> existing = studentRepository.findById(id);
        if (existing.isPresent()) {
            Classes cls = classRepository.findById(studentDto.getCls_id())
                    .orElseThrow(() -> new RuntimeException("Class not found"));
            School school = schoolRepository.findById(studentDto.getSchool_id())
                    .orElseThrow(() -> new RuntimeException("School not found"));
            var student=existing.get();
            student.setId(id);
            student.setName(studentDto.getName());
            student.setDateOfBirth(studentDto.getDateOfBirth());
            student.setFatherName(studentDto.getFatherName());
            student.setMotherName(studentDto.getMotherName());
            student.setAddress(studentDto.getAddress());
            student.setEmail(studentDto.getEmail());
            student.setBatch(studentDto.getBatch());
            student.setCls(cls);
            student.setSchool(school);
            return studentRepository.save(student);
        }
        return null;
    }
}
