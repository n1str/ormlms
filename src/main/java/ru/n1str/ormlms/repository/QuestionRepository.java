package ru.n1str.ormlms.repository;

import ru.n1str.ormlms.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}


