package ru.n1str.ormlms.service;

import ru.n1str.ormlms.entity.Course;
import ru.n1str.ormlms.entity.CourseReview;
import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.exception.NotFoundException;
import ru.n1str.ormlms.repository.CourseRepository;
import ru.n1str.ormlms.repository.CourseReviewRepository;
import ru.n1str.ormlms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseReviewService {

    private final CourseReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    public CourseReview addReview(Long courseId, Long studentId, int rating, String comment) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));

        CourseReview review = new CourseReview();
        review.setCourse(course);
        review.setStudent(student);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<CourseReview> getReviewsForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));
        return reviewRepository.findByCourse(course);
    }
}


