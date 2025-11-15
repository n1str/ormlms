package ru.n1str.ormlms.service;

import ru.n1str.ormlms.entity.Category;
import ru.n1str.ormlms.entity.Course;
import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.exception.NotFoundException;
import ru.n1str.ormlms.model.UserRole;
import ru.n1str.ormlms.repository.CategoryRepository;
import ru.n1str.ormlms.repository.CourseRepository;
import ru.n1str.ormlms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CourseService courseService;

    private User teacher;
    private Category category;
    private Course course;

    @BeforeEach
    void setUp() {
        teacher = User.builder()
                .id(1L)
                .name("Dr. Smith")
                .email("smith@example.com")
                .role(UserRole.TEACHER)
                .build();

        category = Category.builder()
                .id(1L)
                .name("Programming")
                .build();

        course = Course.builder()
                .id(1L)
                .title("Java Basics")
                .description("Learn Java from scratch")
                .teacher(teacher)
                .category(category)
                .startDate(LocalDate.now())
                .build();
    }

    @Test
    void createCourse_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course created = courseService.createCourse("Java Basics", "Learn Java from scratch", 1L, 1L, 40, LocalDate.now(), Set.of());

        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Java Basics");
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void createCourse_TeacherNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.createCourse("Java Basics", "Desc", 1L, 1L, 40, LocalDate.now(), Set.of()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Teacher not found");
    }

    @Test
    void getCourseById_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course found = courseService.getCourse(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    void getCourseById_NotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourse(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Course not found");
    }

    @Test
    void deleteCourse_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        courseService.deleteCourse(1L);

        verify(courseRepository).delete(course);
    }

    @Test
    void deleteCourse_NotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.deleteCourse(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Course not found");
    }
}

