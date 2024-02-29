package com.example.uniapi.repository;

import com.example.uniapi.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    Optional<Student> findFirstByEmail(String email);


    Page<Student> findAll (Pageable paging);

    Page<Student> findAllByCourseId(Long courseId, Pageable paging);

    Page<Student> findAllByCourseInstitutionId(Long institutionId, Pageable paging);

}
