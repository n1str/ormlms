package ru.n1str.ormlms.repository;

import ru.n1str.ormlms.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}


