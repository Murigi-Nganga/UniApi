package com.example.uniapi.controller;

import com.example.uniapi.domain.Institution;
import com.example.uniapi.domain.enums.InstitutionType;
import com.example.uniapi.dto.PatchInstitutionDTO;
import com.example.uniapi.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

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
            @RequestParam(name = "type", required = false) InstitutionType type,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") String sortOrder
    ) {

        Sort.Direction direction;
        if(sortOrder.equalsIgnoreCase("ASC")) {
            direction = Sort.Direction.ASC;
        }
        if(sortOrder.equalsIgnoreCase("DESC")) {
            direction = Sort.Direction.DESC;
        } else {
            System.out.println("Invalid sort order value");
            direction = Sort.Direction.ASC;
            //TODO: Show allowed sorted order values
            // throw InvalidSortOrderValueException();
        }

        List<String> institutionClassFields = Stream.of(Institution.class.getFields()).map(Field::getName).toList();

        Sort sort = sortBy == null || sortBy.isEmpty() ? Sort.unsorted() : Sort.by(direction, sortBy);

        if(sortBy != null && !institutionClassFields.contains(sortBy)) {
            //TODO: Show allowed sort field values
            // throw InvalidSortFieldValueException();
            System.out.println("Wrong sort field name");
        }

        List<Institution> institutions =  institutionService.getInstitutions(type, location, sort);

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
    public void deleteInstitution(@PathVariable Long institutionId) throws Exception {
        institutionService.deleteInstitution(institutionId);
    }
}
