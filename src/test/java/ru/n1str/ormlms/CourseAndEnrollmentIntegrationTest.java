package ru.n1str.ormlms;

import ru.n1str.ormlms.entity.*;
import ru.n1str.ormlms.model.EnrollmentStatus;
import ru.n1str.ormlms.model.UserRole;
import ru.n1str.ormlms.repository.*;
import ru.n1str.ormlms.service.CourseService;
import ru.n1str.ormlms.service.EnrollmentService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CourseAndEnrollmentIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void createCourseAndEnrollStudent_crudFlowWorks() {
        User teacher = userRepository.save(
                User.builder().name("Teacher").email("t@example.com").role(UserRole.TEACHER).build()
        );
        User student = userRepository.save(
                User.builder().name("Student").email("s@example.com").role(UserRole.STUDENT).build()
        );
        Category category = categoryRepository.save(Category.builder().name("Databases").build());
        Tag tag = tagRepository.save(Tag.builder().name("Hibernate").build());

        Course course = courseService.createCourse(
                "Hibernate Basics",
                "Intro course",
                category.getId(),
                teacher.getId(),
                10,
                LocalDate.now(),
                Set.of(tag.getId())
        );

        assertThat(course.getId()).isNotNull();

        enrollmentService.enrollStudent(course.getId(), student.getId());

        entityManager.flush();
        entityManager.clear();

        List<Enrollment> enrollments = enrollmentRepository.findAll();
        assertThat(enrollments).hasSize(1);
        Enrollment enrollment = enrollments.get(0);
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
        assertThat(enrollment.getCourse().getId()).isEqualTo(course.getId());
        assertThat(enrollment.getStudent().getId()).isEqualTo(student.getId());

        enrollmentService.unenrollStudent(course.getId(), student.getId());
        entityManager.flush();

        assertThat(enrollmentRepository.count()).isZero();
    }
}


