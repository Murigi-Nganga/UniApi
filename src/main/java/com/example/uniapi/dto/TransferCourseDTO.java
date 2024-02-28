package com.example.uniapi.dto;

// DTO for transferring a Course to a different Institution
// institutionId is the id of new Institution that the Course is being transferred to
public record TransferCourseDTO(Long institutionId) { }
