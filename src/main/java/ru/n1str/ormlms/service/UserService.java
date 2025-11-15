package ru.n1str.ormlms.service;

import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.exception.NotFoundException;
import ru.n1str.ormlms.model.UserRole;
import ru.n1str.ormlms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    public List<User> getByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Transactional
    public User createUser(String name, String email, UserRole role) {
        User user = User.builder()
                .name(name)
                .email(email)
                .role(role)
                .build();
        return userRepository.save(user);
    }
}


