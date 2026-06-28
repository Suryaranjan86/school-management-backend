package com.srs.school.repository;

import com.srs.school.dto.ClassInfoDto;
import com.srs.school.dto.StudentAcademicRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentAcademicRecordRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(StudentAcademicRecordRepository.class);

    /**
     * Find all academic records for a student
     * SELECT * FROM student_academic_record
     * WHERE student_id = ? AND (SELECT school_id FROM student WHERE id = ?) IS NOT NULL
     */
    public List<StudentAcademicRecord> findByStudentId(String studentId, String schoolId) {
        log.info("Entering findByStudentId - studentId: {}, schoolId: {}", studentId, schoolId);
        String sql = "SELECT sar.* FROM student_academic_record sar " +
                "INNER JOIN student s ON sar.student_id = s.id " +
                "WHERE sar.student_id = ? AND s.school_id = ?";
        return jdbcTemplate.query(sql, new StudentAcademicRecordRowMapper(), studentId, schoolId);
    }


    /**
     * Find academic record by student ID, academic year, and class ID
     */
    public Optional<StudentAcademicRecord> findByStudentIdAndAcademicYearAndClsId(
            String studentId, String academicYear, String clsId, String schoolId) {
        log.info("Entering findByStudentIdAndAcademicYearAndClsId - studentId: {}, academicYear: {}, clsId: {}, schoolId: {}",
                studentId, academicYear, clsId, schoolId);
        String sql = "SELECT sar.* FROM student_academic_record sar " +
                "INNER JOIN student s ON sar.student_id = s.id " +
                "WHERE sar.student_id = ? AND sar.academic_year = ? AND sar.cls_id = ? AND s.school_id = ?";
        try {
            StudentAcademicRecord result = jdbcTemplate.queryForObject(sql,
                    new StudentAcademicRecordRowMapper(), studentId, academicYear, clsId, schoolId);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    /**
     * Find all academic records by academic year
     */
    public List<StudentAcademicRecord> findByAcademicYear(String academicYear, String schoolId) {
        log.info("Entering findByAcademicYear - academicYear: {}, schoolId: {}", academicYear, schoolId);
        String sql = "SELECT sar.* FROM student_academic_record sar " +
                "INNER JOIN student s ON sar.student_id = s.id " +
                "WHERE sar.academic_year = ? AND s.school_id = ? AND s.is_deleted = 'N'";
        return jdbcTemplate.query(sql, new StudentAcademicRecordRowMapper(), academicYear, schoolId);
    }


    /**
     * Find all unique academic years by school
     */
    public List<String> findAllUniqueAcademicYearsBySchool(String schoolId, String studentId) {
        log.info("Entering findAllUniqueAcademicYearsBySchool - schoolId: {}, studentId: {}", schoolId, studentId);
        String sql = "SELECT DISTINCT sar.academic_year FROM student_academic_record sar " +
                "INNER JOIN student s ON sar.student_id = s.id " +
                "WHERE s.school_id = ?";

        if (studentId != null && !studentId.trim().isEmpty()) {
            sql += " AND sar.student_id = ?";
            return jdbcTemplate.queryForList(sql, String.class, schoolId, studentId);
        }

        sql += " ORDER BY sar.academic_year DESC";
        return jdbcTemplate.queryForList(sql, String.class, schoolId);
    }

    /**
     * Find all unique classes by school
     */
    public List<ClassInfoDto> findAllUniqueClassesBySchool(String schoolId, String studentId) {
        log.info("Entering findAllUniqueClassesBySchool - schoolId: {}, studentId: {}", schoolId, studentId);
        String sql = "SELECT DISTINCT c.id, c.name FROM student_academic_record sar " +
                "INNER JOIN student s ON sar.student_id = s.id " +
                "INNER JOIN classes c ON sar.cls_id = c.id " +
                "WHERE s.school_id = ?";

        if (studentId != null && !studentId.trim().isEmpty()) {
            sql += " AND sar.student_id = ?";
            sql += " GROUP BY c.id, c.name ORDER BY c.id";
            return jdbcTemplate.query(sql, (rs, rowNum) ->
                            new ClassInfoDto(rs.getString("id"), rs.getString("name")),
                    schoolId, studentId);
        }

        sql += " GROUP BY c.id, c.name ORDER BY c.id";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new ClassInfoDto(rs.getString("id"), rs.getString("name")),
                schoolId);
    }

    /**
     * Save a new student academic record
     */
    public int save(StudentAcademicRecord record) {
        log.info("Entering save StudentAcademicRecord - id: {}", record != null ? record.getId() : null);
        String sql = "INSERT INTO student_academic_record " +
                "(id, academic_year, cls_id, student_id, class_roll_no, is_current_academic_year) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                record.getId(),
                record.getAcademicYear(),
                record.getClsId(),
                record.getStudentId(),
                record.getClassRollNo(),
                record.getIsCurrentAcademicYear());
    }

    /**
     * Update an existing student academic record
     */
    public int update(StudentAcademicRecord record) {
        log.info("Entering update StudentAcademicRecord - id: {}", record != null ? record.getId() : null);
        String sql = "UPDATE student_academic_record SET " +
                "academic_year = ?, cls_id = ?, student_id = ?, " +
                "class_roll_no = ?, is_current_academic_year = ? " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql,
                record.getAcademicYear(),
                record.getClsId(),
                record.getStudentId(),
                record.getClassRollNo(),
                record.getIsCurrentAcademicYear(),
                record.getId());
    }

    /**
     * Save all records (for batch operations)
     */
    public void saveAll(List<StudentAcademicRecord> records) {
        log.info("Entering saveAll StudentAcademicRecord - count: {}", records != null ? records.size() : 0);
        for (StudentAcademicRecord record : records) {
            // Check if record exists
            String checkSql = "SELECT COUNT(*) FROM student_academic_record WHERE id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, record.getId());

            if (count != null && count > 0) {
                update(record);
            } else {
                save(record);
            }
        }
    }

    /**
     * Find record by ID. Only returns the record when the associated student is not deleted
     * (student.is_deleted = 'N'). This joins the student table and filters deleted students.
     */
    public Optional<StudentAcademicRecord> findById(String id) {
        log.info("Entering findById StudentAcademicRecord - id: {}", id);
        String sql = "SELECT sar.* FROM student_academic_record sar " +
                "INNER JOIN student s ON sar.student_id = s.id " +
                "WHERE sar.id = ? AND s.is_deleted = 'N'";
        try {
            StudentAcademicRecord result = jdbcTemplate.queryForObject(sql,
                    new StudentAcademicRecordRowMapper(), id);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    /**
     * RowMapper for mapping ResultSet to StudentAcademicRecord entity
     */
    private static class StudentAcademicRecordRowMapper implements RowMapper<StudentAcademicRecord> {
        @Override
        public StudentAcademicRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            return StudentAcademicRecord.builder()
                    .id(rs.getString("id"))
                    .academicYear(rs.getString("academic_year"))
                    .clsId(rs.getString("cls_id"))
                    .studentId(rs.getString("student_id"))
                    .classRollNo(rs.getString("class_roll_no"))
                    .isCurrentAcademicYear(rs.getString("is_current_academic_year"))
                    .build();
        }
    }
}

