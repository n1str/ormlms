package ru.n1str.ormlms.repository;

import ru.n1str.ormlms.entity.Quiz;
import ru.n1str.ormlms.entity.QuizSubmission;
import ru.n1str.ormlms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {

    List<QuizSubmission> findByStudent(User student);

    List<QuizSubmission> findByQuiz(Quiz quiz);

    Optional<QuizSubmission> findByQuizAndStudent(Quiz quiz, User student);
}


