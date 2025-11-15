package ru.n1str.ormlms.service;

import ru.n1str.ormlms.entity.Course;
import ru.n1str.ormlms.entity.Enrollment;
import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.exception.BusinessException;
import ru.n1str.ormlms.exception.NotFoundException;
import ru.n1str.ormlms.model.EnrollmentStatus;
import ru.n1str.ormlms.repository.CourseRepository;
import ru.n1str.ormlms.repository.EnrollmentRepository;
import ru.n1str.ormlms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Enrollment enrollStudent(Long courseId, Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

        enrollmentRepository.findByStudentAndCourse(student, course)
                .ifPresent(e -> {
                    throw new BusinessException("Student is already enrolled to this course");
                });

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void unenrollStudent(Long courseId, Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new NotFoundException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);
    }

    public List<Course> getCoursesForStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
        return enrollmentRepository.findByStudent(student).stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }

    public List<User> getStudentsForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));
        return enrollmentRepository.findByCourse(course).stream()
                .map(Enrollment::getStudent)
                .collect(Collectors.toList());
    }
}


