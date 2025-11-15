package ru.n1str.ormlms.api;

import ru.n1str.ormlms.api.dto.AssignmentDtos;
import ru.n1str.ormlms.entity.Assignment;
import ru.n1str.ormlms.entity.Submission;
import ru.n1str.ormlms.service.AssignmentService;
import ru.n1str.ormlms.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final SubmissionService submissionService;

    @PostMapping("/lessons/{lessonId}/assignments")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createAssignment(
            @PathVariable Long lessonId,
            @RequestBody @Valid AssignmentDtos.CreateAssignmentRequest request
    ) {
        Assignment assignment = assignmentService.createAssignment(
                lessonId,
                request.title(),
                request.description(),
                request.dueDate(),
                request.maxScore()
        );
        return assignment.getId();
    }

    @PostMapping("/assignments/{assignmentId}/submit")
    @ResponseStatus(HttpStatus.CREATED)
    public Long submit(
            @PathVariable Long assignmentId,
            @RequestBody @Valid AssignmentDtos.SubmitAssignmentRequest request
    ) {
        Submission submission = submissionService.submitAssignment(
                assignmentId,
                request.studentId(),
                request.content()
        );
        return submission.getId();
    }

    @PostMapping("/submissions/{submissionId}/grade")
    public void grade(
            @PathVariable Long submissionId,
            @RequestBody @Valid AssignmentDtos.GradeSubmissionRequest request
    ) {
        submissionService.gradeSubmission(submissionId, request.score(), request.feedback());
    }

    @GetMapping("/assignments/{assignmentId}/submissions")
    public List<Long> submissionsForAssignment(@PathVariable Long assignmentId) {
        return submissionService.getForAssignment(assignmentId).stream()
                .map(Submission::getId)
                .toList();
    }

    @GetMapping("/users/{userId}/submissions")
    public List<Long> submissionsForStudent(@PathVariable Long userId) {
        return submissionService.getForStudent(userId).stream()
                .map(Submission::getId)
                .toList();
    }
}


