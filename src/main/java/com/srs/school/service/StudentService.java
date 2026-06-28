package com.srs.school.service;

import com.srs.school.context.SchoolContext;
import com.srs.school.dto.*;
import com.srs.school.dto.StudentResponse;
import com.srs.school.dto.StudentAcademicRecord;
import com.srs.school.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private StudentAcademicRecordRepository studentAcademicRecordRepository;
    @Autowired
    private SequenceRepository sequenceRepository;


    @Transactional
    public StudentResponse saveStudent(StudentRequestDto studentDto) {
        log.info("Entering saveStudent - studentDto: {}", studentDto);
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            throw new RuntimeException("School ID is required");
        }
        // Generate student ID using StuIdGenerator logic
        String studentId = sequenceRepository.generateStudentId();

        var student = StudentResponse.builder()
                .id(studentId)
                .name(studentDto.getName())
                .dateOfBirth(studentDto.getDateOfBirth())
                .fatherName(studentDto.getFatherName())
                .motherName(studentDto.getMotherName())
                .address(studentDto.getAddress())
                .email(studentDto.getEmail())
                .gender(studentDto.getGender())
                .schoolId(schoolId)
                .build();
        StudentResponse savedStudent = studentRepository.save(student);

        // Handle student academic record
        handleStudentAcademicRecord(savedStudent.getId(), schoolId,
                studentDto.getAcademicYear(), studentDto.getClsId(), studentDto.getClassRollNo());

        return savedStudent;
    }

    public List<ClassCountDto> getStudentsByClassSummary() {
        log.info("Entering getStudentsByClassSummary");
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return List.of();
        }
        List<Object[]> rows = new ArrayList<>();
        List<ClassCountDto> result = new ArrayList<>();
        for (Object[] r : rows) {
            String className = (String) r[0];
            Long count = (Long) r[1];
            result.add(new ClassCountDto(className, count));
        }
        return result;
    }

    public GenderSummaryDto getGenderSummary() {
        log.info("Entering getGenderSummary");
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

    public List<StudentRequestDto> getAllStudents() {
        log.info("Entering getAllStudents");
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return List.of();
        }
        return studentRepository.getAllStudentsDtoBySchoolId(schoolId);
    }

    public StudentRequestDto getStudentById(String id) {
        log.info("Entering getStudentById - id: {}", id);
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return null;
        }
        return studentRepository.getStudentDtoByIdAndSchoolId(id, schoolId).orElse(null);
    }

    public void deleteStudent(String id) {
        log.info("Entering deleteStudent - id: {}", id);
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            throw new RuntimeException("School ID is required");
        }
        Optional<StudentResponse> student = studentRepository.findByIdAndSchoolId(id, schoolId);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Student not found in this school");
        }
    }

    @Transactional
    public StudentResponse updateStudent(String id, StudentRequestDto studentDto) {
        log.info("Entering updateStudent - id: {}, studentDto: {}", id, studentDto);
        String schoolId = SchoolContext.getSchoolId();
        Optional<StudentResponse> existing = studentRepository.findById(id);
        if (existing.isPresent()) {
            var student = existing.get();
            student.setId(id);
            student.setName(studentDto.getName());
            student.setDateOfBirth(studentDto.getDateOfBirth());
            student.setFatherName(studentDto.getFatherName());
            student.setMotherName(studentDto.getMotherName());
            student.setAddress(studentDto.getAddress());
            student.setEmail(studentDto.getEmail());
            student.setGender(studentDto.getGender());
            student.setSchoolId(schoolId);
            StudentResponse updatedStudent = studentRepository.update(student);

            // Handle student academic record
            handleStudentAcademicRecord(updatedStudent.getId(), schoolId,
                    studentDto.getAcademicYear(), studentDto.getClsId(), studentDto.getClassRollNo());

            return updatedStudent;
        }
        throw new RuntimeException("Student not found in this school");
    }

    /**
     * Handles the student academic record logic:
     * 1. Checks if a record exists with matching academicYear and classId
     * 2. If matching record found: marks it as current ('Y')
     * 3. If no matching record found: creates a new record with 'Y' and marks all other records as 'N'
     */
    private void handleStudentAcademicRecord(String studentId, String schoolId, String academicYear,
                                             String clsId, String classRollNo) {
        log.info("Entering handleStudentAcademicRecord - studentId: {}, schoolId: {}, academicYear: {}, clsId: {}, classRollNo: {}",
                studentId, schoolId, academicYear, clsId, classRollNo);
        if (academicYear == null || academicYear.isEmpty()) {
            return; // Skip if academic year is not provided
        }

        // Get all existing academic records for this student
        List<StudentAcademicRecord> existingRecords =
                studentAcademicRecordRepository.findByStudentId(studentId, schoolId);

        if (existingRecords.isEmpty()) {
            // Create initial record if no records exist
            StudentAcademicRecord newRecord = StudentAcademicRecord.builder()
                    .id(sequenceRepository.generateStudentAcademicRecordId())
                    .studentId(studentId)
                    .academicYear(academicYear)
                    .clsId(clsId)
                    .classRollNo(classRollNo)
                    .isCurrentAcademicYear("Y")
                    .build();
            studentAcademicRecordRepository.save(newRecord);
            return;
        }

        // Check if a record exists with matching academicYear and classId
        StudentAcademicRecord matchingRecord = existingRecords.stream()
                .filter(record -> academicYear.equals(record.getAcademicYear()) &&
                        clsId.equals(record.getClsId()))
                .findFirst()
                .orElse(null);

        List<StudentAcademicRecord> recordsToUpdate = new ArrayList<>();

        if (matchingRecord != null) {
            // Found matching record: mark it as current and update others
            matchingRecord.setIsCurrentAcademicYear("Y");
            matchingRecord.setClassRollNo(classRollNo);
            recordsToUpdate.add(matchingRecord);

            // Mark all other records as not current
            for (StudentAcademicRecord other : existingRecords) {
                if (!other.getId().equals(matchingRecord.getId())) {
                    other.setIsCurrentAcademicYear("N");
                    recordsToUpdate.add(other);
                }
            }
        } else {
            // No matching record found: create a new one and mark it as current
            StudentAcademicRecord newRecord = StudentAcademicRecord.builder()
                    .id(sequenceRepository.generateStudentAcademicRecordId())
                    .studentId(studentId)
                    .academicYear(academicYear)
                    .clsId(clsId)
                    .classRollNo(classRollNo)
                    .isCurrentAcademicYear("Y")
                    .build();
            recordsToUpdate.add(newRecord);

            // Mark all existing records as not current
            for (StudentAcademicRecord existing : existingRecords) {
                existing.setIsCurrentAcademicYear("N");
                recordsToUpdate.add(existing);
            }
        }

        // Batch save all updates
        studentAcademicRecordRepository.saveAll(recordsToUpdate);
    }


}
