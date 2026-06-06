package com.srs.school.service;

import com.srs.school.context.SchoolContext;
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
import java.util.ArrayList;
import com.srs.school.dto.ClassCountDto;
import com.srs.school.dto.GenderSummaryDto;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    public Student saveStudent(StudentDto studentDto) {
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            throw new RuntimeException("School ID is required");
        }
        
        Classes cls = classRepository.findById(studentDto.getCls_id())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found"));
        
        var student = Student.builder()
                .name(studentDto.getName())
                .dateOfBirth(studentDto.getDateOfBirth())
                .fatherName(studentDto.getFatherName())
                .motherName(studentDto.getMotherName())
                .address(studentDto.getAddress())
                .email(studentDto.getEmail())
                .batch(studentDto.getBatch())
                .gender(studentDto.getGender())
                .cls(cls)
                .school(school)
                .build();
        return studentRepository.save(student);
    }

    public List<ClassCountDto> getStudentsByClassSummary() {
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return List.of();
        }
        List<Object[]> rows = studentRepository.countStudentsByClass(schoolId);
        List<ClassCountDto> result = new ArrayList<>();
        for (Object[] r : rows) {
            String className = (String) r[0];
            Long count = (Long) r[1];
            result.add(new ClassCountDto(className, count));
        }
        return result;
    }

    public GenderSummaryDto getGenderSummary() {
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return new GenderSummaryDto(0L, 0L);
        }
        List<Object[]> rows = studentRepository.countStudentsByGender(schoolId);
        long male = 0L;
        long female = 0L;
        for (Object[] r : rows) {
            String gender = (String) r[0];
            Long count = (Long) r[1];
            if (gender == null) continue;
            String g = gender.toLowerCase();
            if (g.equals("male") || g.equals("m")) male = count;
            else if (g.equals("female") || g.equals("f")) female = count;
        }
        return new GenderSummaryDto(male, female);
    }

    public List<Student> getAllStudents() {
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return List.of();
        }
        return studentRepository.findBySchoolId(schoolId);
    }

    public Student getStudentById(String id) {
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return null;
        }
        return studentRepository.findByIdAndSchoolId(id, schoolId).orElse(null);
    }

    public void deleteStudent(String id) {
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            throw new RuntimeException("School ID is required");
        }
        Optional<Student> student = studentRepository.findByIdAndSchoolId(id, schoolId);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Student not found in this school");
        }
    }

    public Student updateStudent(String id, StudentDto studentDto) {
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            throw new RuntimeException("School ID is required");
        }
        Optional<Student> existing = studentRepository.findById(id);
        if (existing.isPresent() && existing.get().getSchool() != null && 
            existing.get().getSchool().getId().equals(schoolId)) {
            Classes cls = classRepository.findById(studentDto.getCls_id())
                    .orElseThrow(() -> new RuntimeException("Class not found"));
            School school = schoolRepository.findById(schoolId)
                    .orElseThrow(() -> new RuntimeException("School not found"));
            var student = existing.get();
            student.setId(id);
            student.setName(studentDto.getName());
            student.setDateOfBirth(studentDto.getDateOfBirth());
            student.setFatherName(studentDto.getFatherName());
            student.setMotherName(studentDto.getMotherName());
            student.setAddress(studentDto.getAddress());
            student.setEmail(studentDto.getEmail());
            student.setBatch(studentDto.getBatch());
            student.setGender(studentDto.getGender());
            student.setCls(cls);
            student.setSchool(school);
            return studentRepository.save(student);
        }
        throw new RuntimeException("Student not found in this school");
    }
}
