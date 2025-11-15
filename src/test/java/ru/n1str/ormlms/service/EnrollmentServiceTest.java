package ru.n1str.ormlms.service;

import ru.n1str.ormlms.entity.Course;
import ru.n1str.ormlms.entity.Enrollment;
import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.exception.BusinessException;
import ru.n1str.ormlms.exception.NotFoundException;
import ru.n1str.ormlms.model.EnrollmentStatus;
import ru.n1str.ormlms.model.UserRole;
import ru.n1str.ormlms.repository.CourseRepository;
import ru.n1str.ormlms.repository.EnrollmentRepository;
import ru.n1str.ormlms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private User student;
    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        student = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .role(UserRole.STUDENT)
                .build();

        course = Course.builder()
                .id(1L)
                .title("Java Basics")
                .build();

        enrollment = Enrollment.builder()
                .id(1L)
                .student(student)
                .course(course)
                .enrollDate(LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();
    }

    @Test
    void enrollStudent_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(Optional.empty());
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = enrollmentService.enrollStudent(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void enrollStudent_AlreadyEnrolled() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(Optional.of(enrollment));

        assertThatThrownBy(() -> enrollmentService.enrollStudent(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already enrolled");
    }

    @Test
    void enrollStudent_StudentNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.enrollStudent(999L, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Student not found");
    }

    @Test
    void unenrollStudent_Success() {
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(Optional.of(enrollment));
        doNothing().when(enrollmentRepository).delete(enrollment);

        enrollmentService.unenrollStudent(1L, 1L);

        verify(enrollmentRepository).delete(enrollment);
    }

    @Test
    void unenrollStudent_NotEnrolled() {
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.unenrollStudent(1L, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Enrollment not found");
    }
}

