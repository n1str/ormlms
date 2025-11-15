package ru.n1str.ormlms.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AssignmentDtos {

    public record CreateAssignmentRequest(
            @NotBlank String title,
            String description,
            @NotNull LocalDate dueDate,
            @NotNull Integer maxScore
    ) {
    }

    public record SubmitAssignmentRequest(
            @NotNull Long studentId,
            @NotBlank String content
    ) {
    }

    public record GradeSubmissionRequest(
            @NotNull Integer score,
            String feedback
    ) {
    }
}


