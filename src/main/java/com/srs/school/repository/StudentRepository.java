package com.srs.school.repository;

import com.srs.school.dto.StudentRequestDto;
import com.srs.school.dto.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(StudentRepository.class);

    /**
     * RowMapper for Student entity
     */
    private RowMapper<StudentResponse> studentRowMapper = (rs, rowNum) -> StudentResponse.builder()
            .id(rs.getString("id"))
            .name(rs.getString("name"))
            .fatherName(rs.getString("father_name"))
            .motherName(rs.getString("mother_name"))
            .dateOfBirth(rs.getTimestamp("date_of_birth") != null ? new java.util.Date(rs.getTimestamp("date_of_birth").getTime()) : null)
            .address(rs.getString("address"))
            .email(rs.getString("email"))
            .gender(rs.getString("gender"))
            .build();

    /**
     * RowMapper for StudentDto
     */
    private RowMapper<StudentRequestDto> studentDtoRowMapper = (rs, rowNum) -> new StudentRequestDto(
            rs.getString("student_id"),
            rs.getString("name"),
            rs.getString("father_name"),
            rs.getString("mother_name"),
            rs.getTimestamp("date_of_birth") != null ? new java.util.Date(rs.getTimestamp("date_of_birth").getTime()) : null,
            rs.getString("address"),
            rs.getString("email"),
            rs.getString("academic_year"),
            rs.getString("gender"),
            rs.getString("cls_id") != null ? rs.getString("cls_id") : "",
            rs.getString("cls_name") != null ? rs.getString("cls_name") : "",
            rs.getString("class_roll_no"),
            rs.getString("is_current_academic_year")
    );

    /**
     * Save a new student
     */
    public StudentResponse save(StudentResponse student) {
        log.info("Entering StudentRepository.save - student id: {}", student != null ? student.getId() : null);
        String sql = "INSERT INTO student (id, name, father_name, mother_name, date_of_birth, address, email, gender, school_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql,
                student.getId(),
                student.getName(),
                student.getFatherName(),
                student.getMotherName(),
                student.getDateOfBirth(),
                student.getAddress(),
                student.getEmail(),
                student.getGender(),
                student.getSchoolId()
        );
        
        return student;
    }

    /**
     * Update an existing student
     */
    public StudentResponse update(StudentResponse student) {
        log.info("Entering StudentRepository.update - student id: {}", student != null ? student.getId() : null);
        String sql = "UPDATE student SET name = ?, father_name = ?, mother_name = ?, date_of_birth = ?, " +
                "address = ?, email = ?, gender = ? WHERE id = ?";
        
        jdbcTemplate.update(sql,
                student.getName(),
                student.getFatherName(),
                student.getMotherName(),
                student.getDateOfBirth(),
                student.getAddress(),
                student.getEmail(),
                student.getGender(),
                student.getId()
        );
        
        return student;
    }

    /**
     * Find student by ID
     */
    public Optional<StudentResponse> findById(String id) {
        log.info("Entering StudentRepository.findById - id: {}", id);
        String sql = "SELECT id, name, father_name, mother_name, date_of_birth, address, email, gender, school_id FROM student WHERE id = ? and is_deleted='N'";
        
        try {
            StudentResponse student = jdbcTemplate.queryForObject(sql, studentRowMapper, id);
            return Optional.of(student);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }



    /**
     * Find student by ID and school ID
     */
    public Optional<StudentResponse> findByIdAndSchoolId(String id, String schoolId) {
        log.info("Entering StudentRepository.findByIdAndSchoolId - id: {}, schoolId: {}", id, schoolId);
        String sql = "SELECT id, name, father_name, mother_name, date_of_birth, address, email, gender, school_id FROM student WHERE id = ? AND school_id = ? AND  is_deleted='N'";
        
        try {
            StudentResponse student = jdbcTemplate.queryForObject(sql, studentRowMapper, id, schoolId);
            return Optional.of(student);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Delete student by ID
     */
    public void deleteById(String id) {
        log.info("Entering StudentRepository.deleteById - id: {}", id);
        String sql = "UPDATE student SET is_deleted = 'Y' WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Count students by gender for a school
     */
    public List<Object[]> countStudentsByGender(String schoolId) {
        log.info("Entering StudentRepository.countStudentsByGender - schoolId: {}", schoolId);
        String sql = "SELECT gender, COUNT(*) as count FROM student WHERE school_id = ? AND  is_deleted='N' GROUP BY gender ";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Object[] row = new Object[2];
            row[0] = rs.getString("gender");
            row[1] = rs.getLong("count");
            return row;
        }, schoolId);
    }

    /**
     * Get all students with their academic records as StudentDto
     */
    public List<StudentRequestDto> getAllStudentsDtoBySchoolId(String schoolId) {
        log.info("Entering StudentRepository.getAllStudentsDtoBySchoolId - schoolId: {}", schoolId);
        String sql = "SELECT s.id as student_id, s.name, s.father_name, s.mother_name, s.date_of_birth, s.address, s.email, s.gender, " +
                "sar.academic_year, COALESCE(sar.cls_id, '') as cls_id, COALESCE(c.name, '') as cls_name, " +
                "sar.class_roll_no, sar.is_current_academic_year " +
                "FROM student s " +
                "LEFT JOIN student_academic_record sar ON s.id = sar.student_id AND sar.is_current_academic_year = 'Y' " +
                "LEFT JOIN classes c ON sar.cls_id = c.id " +
                "WHERE s.school_id = ? and s.is_deleted='N'";
        
        return jdbcTemplate.query(sql, studentDtoRowMapper, schoolId);
    }

    /**
     * Get a specific student by ID and school ID as StudentDto
     */
    public Optional<StudentRequestDto> getStudentDtoByIdAndSchoolId(String id, String schoolId) {
        log.info("Entering StudentRepository.getStudentDtoByIdAndSchoolId - id: {}, schoolId: {}", id, schoolId);
        String sql = "SELECT s.id as student_id, s.name, s.father_name, s.mother_name, s.date_of_birth, s.address, s.email, s.gender, " +
                "sar.academic_year, COALESCE(sar.cls_id, '') as cls_id, COALESCE(c.name, '') as cls_name, " +
                "sar.class_roll_no, sar.is_current_academic_year " +
                "FROM student s " +
                "LEFT JOIN student_academic_record sar ON s.id = sar.student_id AND sar.is_current_academic_year = 'Y' " +
                "LEFT JOIN classes c ON sar.cls_id = c.id " +
                "WHERE s.id = ? AND s.school_id = ? and s.is_deleted='N'";
        
        try {
            StudentRequestDto studentDto = jdbcTemplate.queryForObject(sql, studentDtoRowMapper, id, schoolId);
            return Optional.of(studentDto);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
