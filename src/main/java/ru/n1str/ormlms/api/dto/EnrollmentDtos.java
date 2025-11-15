package ru.n1str.ormlms.api.dto;

import jakarta.validation.constraints.NotNull;

public class EnrollmentDtos {

    public record EnrollRequest(
            @NotNull Long studentId
    ) {
    }
}


