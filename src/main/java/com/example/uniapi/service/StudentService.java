package com.example.uniapi.service;

import com.example.uniapi.domain.Course;
import com.example.uniapi.domain.Student;
import com.example.uniapi.dto.CreateStudentDTO;
import com.example.uniapi.dto.PatchStudentDTO;
import com.example.uniapi.dto.TransferStudentDTO;
import com.example.uniapi.repository.CourseRepository;
import com.example.uniapi.repository.StudentRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    @Autowired
    public StudentService(
            StudentRepository studentRepository,
            CourseRepository courseRepository
    ) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    private Student findByIdOrThrow(Long studentId) {
        return studentRepository
                .findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with the ID " +
                        studentId + " doesn't exist"));
    }

    public Student createStudent(CreateStudentDTO createStudentDTO) {
        // Check if the course to be assigned exists
        Optional<Course> course = courseRepository.findById(createStudentDTO.courseId());

        if(course.isEmpty()) {
            throw new EntityNotFoundException("Course with the ID " +
                    createStudentDTO.courseId() + " doesn't exist");
        }

        // Check if there's a student with a similar email
        Optional<Student> student = studentRepository
                .findFirstByEmail(createStudentDTO.email());

        if(student.isPresent()) {
            throw new EntityExistsException("Student with a similar email exists");
        }

        Student newStudent = new Student(
                createStudentDTO.name(),
                createStudentDTO.email(),
                course.get()
        );

        return studentRepository.save(newStudent);
    }

    public Student getStudent(Long studentId) { return findByIdOrThrow(studentId);}

    public List<Student> getStudents(Long institutionId, Long courseId, Sort sort) {
        // Begin with checking courseId because it's in an institution
        // Even if both institutionId and courseId have values,
        // courseId has a higher 'specificity'
        if (courseId != null) {
            // courseId has a value
            return studentRepository.findAllByCourseId(courseId, sort);
        }
        else if (institutionId != null) {
            // institutionId has a value
            return studentRepository.findAllByCourseInstitutionId(institutionId, sort);
        } else {
            // institutionId and courseId are both null
            return studentRepository.findAll(sort);
        }
    }

    public Student updateStudent(Long studentId, PatchStudentDTO patchStudentDTO) {
        // Check if student with the Id exists
        Student existingStudent = findByIdOrThrow(studentId);

        existingStudent.setName(patchStudentDTO.name());
        return studentRepository.save(existingStudent);
    }

    // This method handles both inter- and intra- institution transfers
    // If a student gets transferred to a course that is in another institution,
    // the student implicitly, becomes part of that institution
    // since every course is in an institution
    public Student transferStudent(
            Long studentId,
            TransferStudentDTO transferStudentDTO) {
        // Check if student with the Id exists
        Student existingStudent = findByIdOrThrow(studentId);

        // Check if the course that the student is being transferred
        // to is the same course that the student is currently enrolled in
        if(transferStudentDTO.courseId().equals(existingStudent.getCourse().getId())) {
            return existingStudent;
        }

        // Check if course that the student is being transferred to exists
        Optional<Course> existingCourse = courseRepository
                .findById(transferStudentDTO.courseId());

        if(existingCourse.isEmpty()) {
            throw new EntityNotFoundException("The course with ID" +
                    transferStudentDTO.courseId() +
                    "that the student is being transferred to doesn't exist");
        }

        // Transfer student to new course
        existingStudent.setCourse(existingCourse.get());

        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }
}
