package ru.n1str.ormlms.repository;

import ru.n1str.ormlms.entity.Course;
import ru.n1str.ormlms.entity.Enrollment;
import ru.n1str.ormlms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudent(User student);

    List<Enrollment> findByCourse(Course course);

    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
}


