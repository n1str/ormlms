package ru.n1str.ormlms.api;

import ru.n1str.ormlms.api.dto.EnrollmentDtos;
import ru.n1str.ormlms.entity.Course;
import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/courses/{courseId}/enroll")
    @ResponseStatus(HttpStatus.CREATED)
    public void enroll(
            @PathVariable Long courseId,
            @RequestBody @Valid EnrollmentDtos.EnrollRequest request
    ) {
        enrollmentService.enrollStudent(courseId, request.studentId());
    }

    @DeleteMapping("/courses/{courseId}/enroll")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unenroll(
            @PathVariable Long courseId,
            @RequestBody @Valid EnrollmentDtos.EnrollRequest request
    ) {
        enrollmentService.unenrollStudent(courseId, request.studentId());
    }

    @GetMapping("/users/{userId}/courses")
    public List<Long> coursesForStudent(@PathVariable Long userId) {
        List<Course> courses = enrollmentService.getCoursesForStudent(userId);
        return courses.stream().map(Course::getId).toList();
    }

    @GetMapping("/courses/{courseId}/students")
    public List<Long> studentsForCourse(@PathVariable Long courseId) {
        List<User> students = enrollmentService.getStudentsForCourse(courseId);
        return students.stream().map(User::getId).toList();
    }
}


