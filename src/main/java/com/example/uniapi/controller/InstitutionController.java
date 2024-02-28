package com.example.uniapi.controller;

import com.example.uniapi.domain.Institution;
import com.example.uniapi.domain.enums.InstitutionType;
import com.example.uniapi.dto.PatchInstitutionDTO;
import com.example.uniapi.exception.InvalidReqParamException;
import com.example.uniapi.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    @Autowired
    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Institution> createInstitution(
            @RequestBody Institution newInstitution
    ) {
        Institution createdInstitution = institutionService
                .createInstitution(newInstitution);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstitution);
    }

    @GetMapping(path = {"/{institutionId}", "/{institutionId}/"})
    public ResponseEntity<Institution> getInstitution(
            @PathVariable Long institutionId) {
        Institution institution = institutionService.getInstitution(institutionId);
        return ResponseEntity.status(HttpStatus.OK).body(institution);
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Institution>> getInstitutions(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") String sortOrder
    ) {
        Sort.Direction direction;
        if(sortOrder.equalsIgnoreCase("ASC")) {
            direction = Sort.Direction.ASC;
        }
        else if(sortOrder.equalsIgnoreCase("DESC")) {
            direction = Sort.Direction.DESC;
        } else {
            throw new InvalidReqParamException("Invalid sortOrder value. " +
                    "Allowed values are 'ASC' and 'DESC'");
        }

        if(type != null && !(type.equals("PRIVATE") || type.equals("PUBLIC"))) {
            throw new InvalidReqParamException("Invalid type value. " +
                    "Allowed values are 'PRIVATE' and 'PUBLIC'");
        }

        List<String> allowedSortFields = List.of("id", "name", "type", "location");

        Sort sort = sortBy == null || sortBy.isEmpty() ? Sort.unsorted() : Sort.by(direction, sortBy);

        if(sortBy != null && !allowedSortFields.contains(sortBy)) {
            throw new InvalidReqParamException(
                    "Invalid sort field name. Allowed sortBy names are: " + allowedSortFields);
        }

        // InstitutionType.valueOf() throws an error with a null value
        List<Institution> institutions =  institutionService
                .getInstitutions(type == null ? null : InstitutionType.valueOf(type), location, sort);
        return ResponseEntity.status(HttpStatus.OK).body(institutions);
    }

    @PatchMapping(path = {"/{institutionId}", "/{institutionId}/"})
    public ResponseEntity<Institution> updateInstitutionName(
            @PathVariable Long institutionId,
            @RequestBody PatchInstitutionDTO patchInstitutionDTO
    ) {
        Institution updatedInstitution = institutionService
                .updateInstitution(institutionId, patchInstitutionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedInstitution);
    }

    @DeleteMapping(path = {"/{institutionId}", "/{institutionId}/"})
    public void deleteInstitution(@PathVariable Long institutionId) {
        institutionService.deleteInstitution(institutionId);
    }
}
