package com.example.uniapi.service;

import com.example.uniapi.domain.Course;
import com.example.uniapi.domain.Institution;
import com.example.uniapi.dto.CreateCourseDTO;
import com.example.uniapi.dto.PatchCourseDTO;
import com.example.uniapi.repository.CourseRepository;
import com.example.uniapi.repository.InstitutionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

        //TODO: Throw a custom CourseNotFoundException
        return courseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Course createCourse(CreateCourseDTO createCourseDTO) throws Exception{
        Institution institution = institutionRepository
                .findById(createCourseDTO.institutionId()).orElseThrow(EntityNotFoundException::new);

        Optional<Course> course = courseRepository.findFirstByNameAndInstitutionId(createCourseDTO.name(), institution.getId());

        if(course.isPresent()) {
            System.out.println("Course with the name " + createCourseDTO.name() + " exists in " + institution.getName());
            throw new Exception("Institution and Course Combination Exists");
            //TODO: Throw InstitutionCourseCominationExists exception
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

        if (institutionId != null) {
            return courseRepository.findAllByInstitutionId(institutionId, sort);
        }
        //TODO: If Institution id not found, throw an EntityNotFoundException
        return courseRepository.findAll(sort);
    }

    public Course updateCourse(Long courseId, PatchCourseDTO patchCourseDTO) throws Exception{
        Course existingCourse = findByIdOrThrow(courseId);

        // Check if there's a course with the new suggested name in the same institution
        Optional<Course> institutionCourse = courseRepository
                .findFirstByNameAndInstitutionId(patchCourseDTO.name(), existingCourse.getInstitution().getId());

        if(institutionCourse.isPresent()) {
            // If institutionCourse has the same name as the new suggested name (in patchCourseDTO),
            // return existingCourse
            if(institutionCourse.get().getName().equals(existingCourse.getName())) {
                return existingCourse;
            }

            throw new Exception("A course with the same name exists in " +
                        existingCourse.getInstitution().getName());

        }

        existingCourse.setName(patchCourseDTO.name());

        return courseRepository.save(existingCourse);
    }

    public void deleteCourse(Long courseId) throws Exception {
        Course course = findByIdOrThrow(courseId);

        //TODO: Only allow deletion if no students are assigned
        //TODO: Throw custom CoursesHasStudentsException
        if(!course.getStudents().isEmpty()) {
            throw new Exception("There are students assigned to this course");
        }

        courseRepository.deleteById(courseId);
    }
}
