package ru.n1str.ormlms.api.dto;

import ru.n1str.ormlms.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDtos {

    public record CreateUserRequest(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotNull UserRole role
    ) {
    }

    public record UserResponse(
            Long id,
            String name,
            String email,
            UserRole role
    ) {
    }
}


