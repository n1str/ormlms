package ru.n1str.ormlms.service;

import ru.n1str.ormlms.entity.Assignment;
import ru.n1str.ormlms.entity.Submission;
import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.exception.BusinessException;
import ru.n1str.ormlms.exception.NotFoundException;
import ru.n1str.ormlms.repository.AssignmentRepository;
import ru.n1str.ormlms.repository.SubmissionRepository;
import ru.n1str.ormlms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Submission submitAssignment(Long assignmentId, Long studentId, String content) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new NotFoundException("Assignment not found: " + assignmentId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));

        submissionRepository.findByAssignmentAndStudent(assignment, student)
                .ifPresent(s -> {
                    throw new BusinessException("Student has already submitted this assignment");
                });

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setContent(content);
        return submissionRepository.save(submission);
    }

    @Transactional
    public Submission gradeSubmission(Long submissionId, Integer score, String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new NotFoundException("Submission not found: " + submissionId));
        submission.setScore(score);
        submission.setFeedback(feedback);
        return submission;
    }

    public List<Submission> getForAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new NotFoundException("Assignment not found: " + assignmentId));
        return submissionRepository.findByAssignment(assignment);
    }

    public List<Submission> getForStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
        return submissionRepository.findByStudent(student);
    }
}


