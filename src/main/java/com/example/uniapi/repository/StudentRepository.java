package com.example.uniapi.repository;

import com.example.uniapi.domain.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findFirstByEmail(String email);

    List<Student> findAllByCourseId(Long courseId, Sort sort);

    List<Student> findAllByCourseInstitutionId(Long institutionId, Sort sort);

}
