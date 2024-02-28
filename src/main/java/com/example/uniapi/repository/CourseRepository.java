package com.example.uniapi.repository;

import com.example.uniapi.domain.Course;
import com.example.uniapi.domain.Institution;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByInstitutionId(Long institutionId, Sort sort);

    Optional<Course> findFirstByNameAndInstitutionId(String name, Long id);
}
