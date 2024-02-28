package com.example.uniapi.controller;

import com.example.uniapi.domain.Course;
import com.example.uniapi.domain.Institution;
import com.example.uniapi.dto.CreateCourseDTO;
import com.example.uniapi.dto.PatchCourseDTO;
import com.example.uniapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

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
        if(sortOrder.equalsIgnoreCase("DESC")) {
            direction = Sort.Direction.DESC;
        } else {
            System.out.println("Invalid sort order value");
            direction = Sort.Direction.ASC;
            //TODO: Show allowed sorted order values
            // throw InvalidSortOrderValueException();
        }

        List<String> courseClassFields = Stream.of(Course.class.getFields()).map(Field::getName).toList();

        Sort sort = sortBy == null || sortBy.isEmpty() ? Sort.unsorted() : Sort.by(direction, sortBy);

        if(sortBy != null && !courseClassFields.contains(sortBy)) {
            //TODO: Show allowed sort field values
            // throw InvalidSortFieldValueException();
            System.out.println("Wrong sort field name");
        }

        List<Course> courses = courseService.getCourses(institutionId, sort);
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @PatchMapping(path = {"/{courseId}", "/{courseId}/"})
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long courseId,
            @RequestBody PatchCourseDTO patchCourseDTO
    ) throws Exception {
        Course updatedCourse = courseService
                .updateCourse(courseId, patchCourseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCourse);
    }



    @DeleteMapping(path = {"/{courseId}", "/{courseId}/"})
    public void deleteCourse(@PathVariable Long courseId) throws Exception {
        courseService.deleteCourse(courseId);
    }

}
