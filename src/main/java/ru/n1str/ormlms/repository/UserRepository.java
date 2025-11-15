package ru.n1str.ormlms.repository;

import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);
}


