package ru.n1str.ormlms.service;

import ru.n1str.ormlms.entity.Assignment;
import ru.n1str.ormlms.entity.Submission;
import ru.n1str.ormlms.entity.User;
import ru.n1str.ormlms.exception.BusinessException;
import ru.n1str.ormlms.exception.NotFoundException;
import ru.n1str.ormlms.model.UserRole;
import ru.n1str.ormlms.repository.AssignmentRepository;
import ru.n1str.ormlms.repository.SubmissionRepository;
import ru.n1str.ormlms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubmissionService submissionService;

    private User student;
    private Assignment assignment;
    private Submission submission;

    @BeforeEach
    void setUp() {
        student = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .role(UserRole.STUDENT)
                .build();

        assignment = Assignment.builder()
                .id(1L)
                .title("Homework 1")
                .maxScore(100)
                .build();

        submission = Submission.builder()
                .id(1L)
                .assignment(assignment)
                .student(student)
                .content("My solution")
                .submittedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void submitAssignment_Success() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(submissionRepository.findByAssignmentAndStudent(assignment, student)).thenReturn(Optional.empty());
        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);

        Submission result = submissionService.submitAssignment(1L, 1L, "My solution");

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("My solution");
        verify(submissionRepository).save(any(Submission.class));
    }

    @Test
    void submitAssignment_AlreadySubmitted() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(submissionRepository.findByAssignmentAndStudent(assignment, student)).thenReturn(Optional.of(submission));

        assertThatThrownBy(() -> submissionService.submitAssignment(1L, 1L, "Another solution"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already submitted");
    }

    @Test
    void gradeSubmission_Success() {
        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));
        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);

        Submission graded = submissionService.gradeSubmission(1L, 95, "Excellent work");

        assertThat(graded.getScore()).isEqualTo(95);
        assertThat(graded.getFeedback()).isEqualTo("Excellent work");
        verify(submissionRepository).save(submission);
    }

    @Test
    void gradeSubmission_NotFound() {
        when(submissionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> submissionService.gradeSubmission(999L, 95, "Good"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Submission not found");
    }
}

