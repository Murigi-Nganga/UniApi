package com.example.uniapi.controller;

import com.example.uniapi.domain.Course;
import com.example.uniapi.dto.CreateCourseDTO;
import com.example.uniapi.dto.PatchCourseDTO;
import com.example.uniapi.exception.InvalidReqParamException;
import com.example.uniapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Course> createCourse(
            @RequestBody CreateCourseDTO createCourseDTO
            ) throws Exception {
        Course createdCourse = courseService
                .createCourse(createCourseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @GetMapping(path = {"/{courseId}", "/{courseId}/"})
    public ResponseEntity<Course> getCourse(
            @PathVariable Long courseId) {
        Course course = courseService.getCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(course);
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Course>> getCourse(
            @RequestParam(name = "institutionId", required = false) Long institutionId,
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

        List<Course> courses = courseService.getCourses(institutionId, sort);
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @PatchMapping(path = {"/{courseId}", "/{courseId}/"})
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long courseId,
            @RequestBody PatchCourseDTO patchCourseDTO
    ) {
        Course updatedCourse = courseService
                .updateCourse(courseId, patchCourseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCourse);
    }

    @DeleteMapping(path = {"/{courseId}", "/{courseId}/"})
    public void deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
    }

}
