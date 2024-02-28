package com.example.uniapi.repository;

import com.example.uniapi.domain.Institution;
import com.example.uniapi.domain.enums.InstitutionType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    Optional<Institution> findByName(String name);

    List<Institution> findAllByType(InstitutionType institutionType, Sort sort);

    List<Institution> findAllByLocation(String location, Sort sort);

    List<Institution> findAllByTypeAndLocation(InstitutionType type, String location, Sort sort);

}
