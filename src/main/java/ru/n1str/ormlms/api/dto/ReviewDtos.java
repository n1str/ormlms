package ru.n1str.ormlms.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewDtos {

    public record AddReviewRequest(
            @NotNull Long studentId,
            @Min(1) @Max(5) int rating,
            @NotBlank String comment
    ) {
    }
}


