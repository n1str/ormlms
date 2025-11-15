package ru.n1str.ormlms;

import ru.n1str.ormlms.entity.Assignment;
import ru.n1str.ormlms.entity.Category;
import ru.n1str.ormlms.entity.Course;
import ru.n1str.ormlms.entity.Lesson;
import ru.n1str.ormlms.entity.Module;
import ru.n1str.ormlms.entity.Submission;
import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.model.UserRole;
import ru.n1str.ormlms.repository.*;
import ru.n1str.ormlms.service.AssignmentService;
import ru.n1str.ormlms.service.CourseService;
import ru.n1str.ormlms.service.SubmissionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AssignmentAndSubmissionIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SubmissionService submissionService;

    @Test
    @Transactional
    void assignmentLifecycle_createSubmitGrade() {
        User teacher = userRepository.save(
                User.builder().name("Teacher2").email("t2@example.com").role(UserRole.TEACHER).build()
        );
        User student = userRepository.save(
                User.builder().name("Student2").email("s2@example.com").role(UserRole.STUDENT).build()
        );
        Category category = categoryRepository.save(Category.builder().name("ORM").build());

        Course course = courseService.createCourse(
                "Hibernate Advanced",
                "Advanced features",
                category.getId(),
                teacher.getId(),
                15,
                LocalDate.now(),
                Set.of()
        );

        Module module = moduleRepository.save(
                Module.builder().course(course).title("Module 1").orderIndex(1).build()
        );
        Lesson lesson = lessonRepository.save(
                Lesson.builder().module(module).title("Lesson 1").content("Content").build()
        );

        Assignment assignment = assignmentService.createAssignment(
                lesson.getId(),
                "HW1",
                "Do something",
                LocalDate.now().plusDays(3),
                100
        );

        Submission submission = submissionService.submitAssignment(
                assignment.getId(),
                student.getId(),
                "My solution"
        );

        submissionService.gradeSubmission(submission.getId(), 95, "Good job");

        List<Submission> byAssignment = submissionService.getForAssignment(assignment.getId());
        assertThat(byAssignment).hasSize(1);
        Submission stored = byAssignment.get(0);
        assertThat(stored.getScore()).isEqualTo(95);
        assertThat(stored.getFeedback()).isEqualTo("Good job");
    }
}


