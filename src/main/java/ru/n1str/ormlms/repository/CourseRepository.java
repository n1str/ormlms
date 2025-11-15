package ru.n1str.ormlms.repository;

import ru.n1str.ormlms.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCategory_Name(String categoryName);

    @EntityGraph(attributePaths = {"modules", "modules.lessons"})
    Optional<Course> findWithStructureById(Long id);
}


