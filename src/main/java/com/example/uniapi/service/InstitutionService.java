package com.example.uniapi.service;

import com.example.uniapi.domain.Institution;
import com.example.uniapi.domain.enums.InstitutionType;
import com.example.uniapi.dto.PatchInstitutionDTO;
import com.example.uniapi.repository.InstitutionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    @Autowired
    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    private Institution findByIdOrThrow(Long institutionId) {
        //TODO: Throw a custom InstitutionNotFoundException
        return institutionRepository.findById(institutionId)
                .orElseThrow(EntityNotFoundException::new);
    }

//    private Optional<Institution> findByName(String name) {
//        return institutionRepository.findByName(name);
//    }

    public Institution createInstitution(Institution newInstitution) {
        //TODO: Impl findByName method here
//        Optional<Institution> institution = institutionRepository.findByName(newInstitution.getName());
//
//        if(institution.isPresent()) {
//            throw new InstitutionExistsException();
//        }

        return institutionRepository.save(newInstitution);
    }

    public Institution getInstitution(Long institutionId) {
        return findByIdOrThrow(institutionId);
    }

    public List<Institution> getInstitutions(InstitutionType type, String location, Sort sort) {
        if (type != null && location != null) {
            // Both type and location have values
            return institutionRepository.findAllByTypeAndLocation(type, location, sort);
        } else if (type != null) {
            // type has a value
            return institutionRepository.findAllByType(type, sort);
        } else if (location != null) {
            // location has a value
            return institutionRepository.findAllByLocation(location, sort);
        } else {
            return institutionRepository.findAll(sort);
        }
    }

    public Institution updateInstitution(Long institutionId, PatchInstitutionDTO patchInstitutionDTO) {
        Institution institution = findByIdOrThrow(institutionId);

        //TODO: Add exception in the event that the new and existing names are the same
        institution.setName(patchInstitutionDTO.name());

        return institutionRepository.save(institution);
    }

    public void deleteInstitution(Long institutionId) {
        Institution institution = findByIdOrThrow(institutionId);

        //TODO: Only allow deletion if no courses are assigned
        //TODO: Throw custom InstitutionHasCoursesException
//        if(institution.getCourses().size() > 0) {
//            throw new InstitutionHasCoursesException();
//        }

        institutionRepository.deleteById(institutionId);
    }
}
