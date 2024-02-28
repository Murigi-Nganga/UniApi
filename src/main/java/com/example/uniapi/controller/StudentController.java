package com.example.uniapi.controller;

import com.example.uniapi.domain.Student;
import com.example.uniapi.dto.CreateStudentDTO;
import com.example.uniapi.dto.PatchStudentDTO;
import com.example.uniapi.dto.TransferStudentDTO;
import com.example.uniapi.exception.InvalidReqParamException;
import com.example.uniapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Student> createStudent(
            @RequestBody CreateStudentDTO createStudentDTO
            ) {
        Student createdStudent = studentService.createStudent(createStudentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping(path = {"/{studentId}", "/{studentId}/"})
    public ResponseEntity<Student> getStudent(
            @PathVariable Long studentId
    ) {
        Student student = studentService.getStudent(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(student);
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Student>> getStudents(
            @RequestParam(name = "institutionId", required = false) Long institutionId,
            @RequestParam(name = "courseId", required = false) Long courseId,
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

        List<String> allowedSortFields = List.of("id", "name");

        Sort sort = sortBy == null || sortBy.isEmpty() ? Sort.unsorted() : Sort.by(direction, sortBy);

        if(sortBy != null && !allowedSortFields.contains(sortBy)) {
            throw new InvalidReqParamException(
                    "Invalid sort field name. Allowed sortBy names are: " + allowedSortFields);
        }

        List<Student> students = studentService.getStudents(courseId, institutionId, sort);
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @PatchMapping(path = {"/{studentId}", "/{studentId}/"})
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long studentId,
            @RequestBody PatchStudentDTO patchStudentDTO
            ) {
        Student updatedStudent = studentService.updateStudent(studentId, patchStudentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedStudent);
    }

    @PatchMapping(path = {"/transfer/{studentId}", "/transfer/{studentId}/"})
    public ResponseEntity<Student> transferStudent(
            @PathVariable Long studentId,
            @RequestBody TransferStudentDTO transferStudentDTO
            ) {
        Student updatedStudent = studentService
                .transferStudent(studentId, transferStudentDTO);

        return ResponseEntity.status(HttpStatus.OK).body(updatedStudent);

    }

    @DeleteMapping(path = {"/{studentId}", "/{studentId}/"})
    public void deleteStudent(
            @PathVariable Long studentId
    ) {
        studentService.deleteStudent(studentId);
    }


}
