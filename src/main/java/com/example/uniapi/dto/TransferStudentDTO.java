package com.example.uniapi.dto;

// DTO for transferring a Student to a different Course
// courseId is the id of Course that the Student is being transferred to
public record TransferStudentDTO(Long courseId) { }
