package ru.n1str.ormlms.config;

import ru.n1str.ormlms.entity.*;
import ru.n1str.ormlms.model.QuestionType;
import ru.n1str.ormlms.model.UserRole;
import ru.n1str.ormlms.repository.*;
import ru.n1str.ormlms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DemoDataConfig {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CourseService courseService;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final AssignmentRepository assignmentRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    @Bean
    CommandLineRunner demoDataRunner() {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            User teacher = userRepository.save(
                    User.builder()
                            .name("Hibernate Teacher")
                            .email("teacher@example.com")
                            .role(UserRole.TEACHER)
                            .build()
            );

            User student = userRepository.save(
                    User.builder()
                            .name("Student One")
                            .email("student@example.com")
                            .role(UserRole.STUDENT)
                            .build()
            );

            Category category = categoryRepository.save(
                    Category.builder().name("Programming").build()
            );

            Tag javaTag = tagRepository.save(Tag.builder().name("Java").build());
            Tag hibernateTag = tagRepository.save(Tag.builder().name("Hibernate").build());

            Course course = courseService.createCourse(
                    "Основы Hibernate",
                    "Учебный курс по ORM и Hibernate",
                    category.getId(),
                    teacher.getId(),
                    20,
                    LocalDate.now(),
                    Set.of(javaTag.getId(), hibernateTag.getId())
            );

            Module module1 = moduleRepository.save(
                    Module.builder()
                            .course(course)
                            .title("Введение в ORM")
                            .orderIndex(1)
                            .build()
            );
            Lesson lesson1 = lessonRepository.save(
                    Lesson.builder()
                            .module(module1)
                            .title("Что такое ORM")
                            .content("Основные понятия ORM и Hibernate")
                            .build()
            );

            assignmentRepository.save(
                    Assignment.builder()
                            .lesson(lesson1)
                            .title("Домашнее задание 1")
                            .description("Ответить на вопросы по ORM")
                            .dueDate(LocalDate.now().plusDays(7))
                            .maxScore(100)
                            .build()
            );

            Quiz quiz = quizRepository.save(
                    Quiz.builder()
                            .module(module1)
                            .title("Тест по введению в ORM")
                            .timeLimitMinutes(15)
                            .build()
            );

            Question q1 = questionRepository.save(
                    Question.builder()
                            .quiz(quiz)
                            .text("Что означает ORM?")
                            .type(QuestionType.SINGLE_CHOICE)
                            .build()
            );

            answerOptionRepository.saveAll(List.of(
                    AnswerOption.builder().question(q1).text("Object-Relational Mapping").correct(true).build(),
                    AnswerOption.builder().question(q1).text("Object-Remote Model").correct(false).build(),
                    AnswerOption.builder().question(q1).text("Only-Relational Model").correct(false).build()
            ));
        };
    }
}


