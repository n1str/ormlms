package ru.n1str.ormlms.repository;

import ru.n1str.ormlms.entity.Course;
import ru.n1str.ormlms.entity.CourseReview;
import ru.n1str.ormlms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

    List<CourseReview> findByCourse(Course course);

    List<CourseReview> findByStudent(User student);
}


