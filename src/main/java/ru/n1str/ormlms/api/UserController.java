package ru.n1str.ormlms.api;

import ru.n1str.ormlms.api.dto.UserDtos;
import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.model.UserRole;
import ru.n1str.ormlms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtos.UserResponse create(@RequestBody @Valid UserDtos.CreateUserRequest request) {
        User user = userService.createUser(request.name(), request.email(), request.role());
        return new UserDtos.UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    @GetMapping("/{id}")
    public UserDtos.UserResponse get(@PathVariable Long id) {
        User user = userService.getById(id);
        return new UserDtos.UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    @GetMapping
    public List<UserDtos.UserResponse> listByRole(@RequestParam(required = false) UserRole role) {
        List<User> users = role != null ? userService.getByRole(role) : userService.getByRole(UserRole.STUDENT);
        return users.stream()
                .map(u -> new UserDtos.UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole()))
                .toList();
    }
}


