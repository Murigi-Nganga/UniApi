package com.example.uniapi.service;

import com.example.uniapi.domain.Institution;
import com.example.uniapi.domain.enums.InstitutionType;
import com.example.uniapi.dto.PatchInstitutionDTO;
import com.example.uniapi.repository.InstitutionRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        return institutionRepository.findById(institutionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Institution with the ID " + institutionId +
                                " does not exist"));
    }

    public Institution createInstitution(Institution newInstitution) {
        Optional<Institution> institution = institutionRepository
                .findByName(newInstitution.getName());

        if(institution.isPresent()) {
            throw new EntityExistsException("Institution with a similar name " +
                    institution.get().getName() + " exists");
        }

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
            // location and type are both null
            return institutionRepository.findAll(sort);
        }
    }

    public Institution updateInstitution(Long institutionId, PatchInstitutionDTO patchInstitutionDTO) {
        Institution existingInstitution = findByIdOrThrow(institutionId);

        // if existingInstitution has the same name with the new suggested name
        // return the existing institution (no db change occurs)
        if(existingInstitution.getName().equals(patchInstitutionDTO.name())) {
            return existingInstitution;
        }

        // Check if there's an institution with the new suggested name
        Optional<Institution> institution = institutionRepository.findByName(patchInstitutionDTO.name());

        if(institution.isPresent()) {
            throw new EntityExistsException("An institution with a similar name '" +
                    patchInstitutionDTO.name() + "' exists");
        }

        // Update the institution if no institution in the db has the new name
        existingInstitution.setName(patchInstitutionDTO.name());
        return institutionRepository.save(existingInstitution);
    }

    public void deleteInstitution(Long institutionId) {
        Institution institution = findByIdOrThrow(institutionId);

        //Only allow deletion if no courses
        if(!institution.getCourses().isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Cannot delete institution because it has assigned courses");
        }

        institutionRepository.deleteById(institutionId);
    }
}
