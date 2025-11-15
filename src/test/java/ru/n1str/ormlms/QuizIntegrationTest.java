package ru.n1str.ormlms;

import ru.n1str.ormlms.entity.*;
import ru.n1str.ormlms.model.QuestionType;
import ru.n1str.ormlms.model.UserRole;
import ru.n1str.ormlms.repository.*;
import ru.n1str.ormlms.service.CourseService;
import ru.n1str.ormlms.service.QuizService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QuizIntegrationTest {

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
    private QuizService quizService;
    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Test
    @Transactional
    void quizLifecycle_createQuestionsTakeQuiz() {
        User teacher = userRepository.save(
                User.builder().name("Teacher3").email("t3@example.com").role(UserRole.TEACHER).build()
        );
        User student = userRepository.save(
                User.builder().name("Student3").email("s3@example.com").role(UserRole.STUDENT).build()
        );
        Category category = categoryRepository.save(Category.builder().name("JPA").build());

        Course course = courseService.createCourse(
                "JPA Intro",
                "Basics of JPA",
                category.getId(),
                teacher.getId(),
                8,
                LocalDate.now(),
                Set.of()
        );

        Module module = moduleRepository.save(
                Module.builder().course(course).title("M1").orderIndex(1).build()
        );
        lessonRepository.save(
                Lesson.builder().module(module).title("L1").content("Content").build()
        );

        Quiz quiz = quizService.createQuiz(module.getId(), "Quiz1", 10);
        Question q1 = quizService.addQuestion(quiz.getId(), "What is JPA?", QuestionType.SINGLE_CHOICE);

        AnswerOption correct = quizService.addAnswerOption(q1.getId(), "Java Persistence API", true);
        quizService.addAnswerOption(q1.getId(), "Java Platform API", false);

        quizService.takeQuiz(quiz.getId(), student.getId(), Map.of(q1.getId(), List.of(correct.getId())));

        List<QuizSubmission> submissions = quizSubmissionRepository.findByQuiz(quiz);
        assertThat(submissions).hasSize(1);
        assertThat(submissions.get(0).getScore()).isEqualTo(100);
    }
}


