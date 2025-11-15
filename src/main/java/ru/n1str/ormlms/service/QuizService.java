package ru.n1str.ormlms.service;

import ru.n1str.ormlms.entity.*;
import ru.n1str.ormlms.exception.BusinessException;
import ru.n1str.ormlms.exception.NotFoundException;
import ru.n1str.ormlms.model.QuestionType;
import ru.n1str.ormlms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;
    private final ModuleRepository moduleRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Quiz createQuiz(Long moduleId, String title, Integer timeLimitMinutes) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Module not found: " + moduleId));
        if (module.getQuiz() != null) {
            throw new BusinessException("Module already has a quiz");
        }
        Quiz quiz = new Quiz();
        quiz.setModule(module);
        quiz.setTitle(title);
        quiz.setTimeLimitMinutes(timeLimitMinutes);
        return quizRepository.save(quiz);
    }

    @Transactional
    public Question addQuestion(Long quizId, String text, QuestionType type) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found: " + quizId));
        Question question = new Question();
        question.setQuiz(quiz);
        question.setText(text);
        question.setType(type);
        return questionRepository.save(question);
    }

    @Transactional
    public AnswerOption addAnswerOption(Long questionId, String text, boolean correct) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found: " + questionId));
        AnswerOption option = new AnswerOption();
        option.setQuestion(question);
        option.setText(text);
        option.setCorrect(correct);
        return answerOptionRepository.save(option);
    }

    @Transactional
    public QuizSubmission takeQuiz(Long quizId, Long studentId, Map<Long, List<Long>> answers) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found: " + quizId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));

        quizSubmissionRepository.findByQuizAndStudent(quiz, student)
                .ifPresent(s -> {
                    throw new BusinessException("Quiz already taken by this student");
                });

        int totalQuestions = quiz.getQuestions().size();
        if (totalQuestions == 0) {
            throw new BusinessException("Quiz has no questions");
        }

        int correctCount = 0;
        for (Question question : quiz.getQuestions()) {
            List<Long> chosen = answers.getOrDefault(question.getId(), Collections.emptyList());
            Set<Long> chosenSet = new HashSet<>(chosen);

            Set<Long> correctOptionIds = question.getOptions().stream()
                    .filter(AnswerOption::isCorrect)
                    .map(AnswerOption::getId)
                    .collect(Collectors.toSet());

            Set<Long> allCorrectAndOnlyCorrect = new HashSet<>(correctOptionIds);
            if (!chosenSet.equals(allCorrectAndOnlyCorrect)) {
                continue;
            }
            correctCount++;
        }

        int score = (int) Math.round((correctCount * 100.0) / totalQuestions);

        QuizSubmission submission = new QuizSubmission();
        submission.setQuiz(quiz);
        submission.setStudent(student);
        submission.setScore(score);
        submission.setTakenAt(LocalDateTime.now());
        return quizSubmissionRepository.save(submission);
    }

    public List<QuizSubmission> getSubmissionsForQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found: " + quizId));
        return quizSubmissionRepository.findByQuiz(quiz);
    }

    public List<QuizSubmission> getSubmissionsForStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
        return quizSubmissionRepository.findByStudent(student);
    }
}


