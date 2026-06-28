package com.srs.school.repository;

import com.srs.school.dto.ClassStudentCountDto;
import com.srs.school.dto.SchoolStudentCountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Repository
public class DashboardRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(DashboardRepository.class);

    public SchoolStudentCountDto getSchoolWiseStudentCount(String schoolId) {
        log.info("Entering getSchoolWiseStudentCount - schoolId: {}", schoolId);
        if (schoolId == null || schoolId.isEmpty()) {
            return new SchoolStudentCountDto(0L, 0L, 0L);
        }

        String sql = "SELECT COUNT(*) AS total_count, " +
                "COALESCE(SUM(CASE WHEN LOWER(gender) = 'male' OR LOWER(gender) = 'm' THEN 1 ELSE 0 END), 0) AS male_count, " +
                "COALESCE(SUM(CASE WHEN LOWER(gender) = 'female' OR LOWER(gender) = 'f' THEN 1 ELSE 0 END), 0) AS female_count " +
                "FROM student WHERE school_id = ? AND is_deleted = 'N'";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new SchoolStudentCountDto(
                rs.getLong("total_count"),
                rs.getLong("male_count"),
                rs.getLong("female_count")
        ), schoolId);
    }

    public List<ClassStudentCountDto> getClassWiseCurrentAcademicSessionStudentCount(String schoolId) {
        log.info("Entering getClassWiseCurrentAcademicSessionStudentCount - schoolId: {}", schoolId);
        if (schoolId == null || schoolId.isEmpty()) {
            return List.of();
        }

        String sql = "SELECT c.name AS class_name, " +
                "COALESCE(SUM(CASE WHEN LOWER(s.gender) = 'male' OR LOWER(s.gender) = 'm' THEN 1 ELSE 0 END), 0) AS male_count, " +
                "COALESCE(SUM(CASE WHEN LOWER(s.gender) = 'female' OR LOWER(s.gender) = 'f' THEN 1 ELSE 0 END), 0) AS female_count, " +
                "COUNT(*) AS total_count " +
                "FROM student_academic_record sar " +
                "JOIN student s ON sar.student_id = s.id " +
                "JOIN classes c ON sar.cls_id = c.id " +
                "WHERE s.school_id = ? AND s.is_deleted = 'N' AND sar.is_current_academic_year = 'Y' " +
                "GROUP BY c.id, c.name " +
                "ORDER BY c.name";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ClassStudentCountDto(
                rs.getString("class_name"),
                rs.getLong("male_count"),
                rs.getLong("female_count"),
                rs.getLong("total_count")
        ), schoolId);
    }
}
