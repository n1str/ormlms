package ru.n1str.ormlms.repository;

import ru.n1str.ormlms.entity.Quiz;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @EntityGraph(attributePaths = {"questions", "questions.options"})
    Optional<Quiz> findById(Long id);
}


