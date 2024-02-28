package com.example.uniapi.service;

import com.example.uniapi.domain.Course;
import com.example.uniapi.domain.Institution;
import com.example.uniapi.dto.CreateCourseDTO;
import com.example.uniapi.dto.PatchCourseDTO;
import com.example.uniapi.repository.CourseRepository;
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
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, InstitutionRepository institutionRepository) {
        this.courseRepository = courseRepository;
        this.institutionRepository = institutionRepository;
    }

    private Course findByIdOrThrow(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course with the ID " +
                        courseId + " doesn't exist"));
    }

    public Course createCourse(CreateCourseDTO createCourseDTO) {
        Institution institution = institutionRepository
                .findById(createCourseDTO.institutionId())
                .orElseThrow(() -> new EntityNotFoundException("Institution with the ID " +
                        createCourseDTO.institutionId() + " doesn't exist"));

        Optional<Course> course = courseRepository
                .findFirstByNameAndInstitutionId(createCourseDTO.name(), institution.getId());

        if(course.isPresent()) {
            throw new EntityExistsException("Course with the name '" +
                    createCourseDTO.name() + "' exists in " + institution.getName() +
                    ". Select a different course name or institution");
        }

        Course newCourse = new Course(
                createCourseDTO.name(),
                createCourseDTO.description(),
                institution
        );

        return courseRepository.save(newCourse);
    }

    public Course getCourse(Long courseId) { return findByIdOrThrow(courseId); }

    public List<Course> getCourses(Long institutionId, Sort sort) {

        // If Request Parameter institutionId has a value
        if (institutionId != null) {
            Optional<Institution> institution = institutionRepository.findById(institutionId);

            if(institution.isEmpty()) {
                throw new EntityNotFoundException("Institution with the ID " +
                        institutionId +  " doesn't exist");
            }

            return courseRepository.findAllByInstitutionId(institutionId, sort);
        }

        return courseRepository.findAll(sort);
    }

    public Course updateCourse(Long courseId, PatchCourseDTO patchCourseDTO) {
        Course existingCourse = findByIdOrThrow(courseId);

        // if existingCourse has the same name with the new suggested name
        // return existingCourse (no db change occurs)
        if(existingCourse.getName().equals(patchCourseDTO.name())) {
            return existingCourse;
        }

        // Check if there's a course with the new suggested name in the same institution
        Optional<Course> institutionCourse = courseRepository
                .findFirstByNameAndInstitutionId(patchCourseDTO.name(), existingCourse.getInstitution().getId());

        if(institutionCourse.isPresent()) {
            throw new EntityExistsException("A course with a similar name exists in " +
                    existingCourse.getInstitution().getName());
        }

        // Update the course name if there's no course with the name in the institution
        existingCourse.setName(patchCourseDTO.name());
        return courseRepository.save(existingCourse);
    }

    public void deleteCourse(Long courseId) {
        Course course = findByIdOrThrow(courseId);

        //Only allow deletion if no students are assigned
        if(!course.getStudents().isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Cannot delete course because it has assigned students");
        }
        courseRepository.deleteById(courseId);
    }
}
