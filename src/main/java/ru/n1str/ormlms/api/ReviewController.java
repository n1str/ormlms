package ru.n1str.ormlms.api;

import ru.n1str.ormlms.api.dto.ReviewDtos;
import ru.n1str.ormlms.entity.CourseReview;
import ru.n1str.ormlms.service.CourseReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class ReviewController {

    private final CourseReviewService reviewService;

    @PostMapping("/{courseId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addReview(
            @PathVariable Long courseId,
            @RequestBody @Valid ReviewDtos.AddReviewRequest request
    ) {
        CourseReview review = reviewService.addReview(courseId, request.studentId(), request.rating(), request.comment());
        return review.getId();
    }

    @GetMapping("/{courseId}/reviews")
    public List<Integer> getRatings(@PathVariable Long courseId) {
        return reviewService.getReviewsForCourse(courseId).stream()
                .map(CourseReview::getRating)
                .toList();
    }
}


