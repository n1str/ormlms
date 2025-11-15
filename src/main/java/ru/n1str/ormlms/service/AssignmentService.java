package ru.n1str.ormlms.service;

import ru.n1str.ormlms.entity.Assignment;
import ru.n1str.ormlms.entity.Lesson;
import ru.n1str.ormlms.exception.NotFoundException;
import ru.n1str.ormlms.repository.AssignmentRepository;
import ru.n1str.ormlms.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    public Assignment createAssignment(Long lessonId, String title, String description, LocalDate dueDate, Integer maxScore) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson not found: " + lessonId));
        Assignment assignment = new Assignment();
        assignment.setLesson(lesson);
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDueDate(dueDate);
        assignment.setMaxScore(maxScore);
        return assignmentRepository.save(assignment);
    }

    public Assignment getById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment not found: " + id));
    }
}


