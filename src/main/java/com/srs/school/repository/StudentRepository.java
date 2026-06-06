package com.srs.school.repository;

import com.srs.school.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findBySchoolId(String schoolId);
    Optional<Student> findByIdAndSchoolId(String id, String schoolId);

    @Query("SELECT c.name, COUNT(s) FROM Student s JOIN s.cls c WHERE s.school.id = :schoolId GROUP BY c.name ORDER BY c.name")
    List<Object[]> countStudentsByClass(@Param("schoolId") String schoolId);

    @Query("SELECT s.gender, COUNT(s) FROM Student s WHERE s.school.id = :schoolId GROUP BY s.gender")
    List<Object[]> countStudentsByGender(@Param("schoolId") String schoolId);
}
