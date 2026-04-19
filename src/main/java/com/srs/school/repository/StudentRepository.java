package com.srs.school.repository;

import com.srs.school.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findBySchoolId(String schoolId);
    Optional<Student> findByIdAndSchoolId(String id, String schoolId);
}
