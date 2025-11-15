package ru.n1str.ormlms.api;

import ru.n1str.ormlms.api.dto.QuizDtos;
import ru.n1str.ormlms.entity.AnswerOption;
import ru.n1str.ormlms.entity.Question;
import ru.n1str.ormlms.entity.Quiz;
import ru.n1str.ormlms.entity.QuizSubmission;
import ru.n1str.ormlms.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/modules/{moduleId}/quiz")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createQuiz(
            @PathVariable Long moduleId,
            @RequestBody @Valid QuizDtos.CreateQuizRequest request
    ) {
        Quiz quiz = quizService.createQuiz(moduleId, request.title(), request.timeLimitMinutes());
        return quiz.getId();
    }

    @PostMapping("/quizzes/{quizId}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addQuestion(
            @PathVariable Long quizId,
            @RequestBody @Valid QuizDtos.CreateQuestionRequest request
    ) {
        Question question = quizService.addQuestion(quizId, request.text(), request.type());
        return question.getId();
    }

    @PostMapping("/questions/{questionId}/options")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addOption(
            @PathVariable Long questionId,
            @RequestBody @Valid QuizDtos.CreateAnswerOptionRequest request
    ) {
        AnswerOption option = quizService.addAnswerOption(questionId, request.text(), request.correct());
        return option.getId();
    }

    @PostMapping("/quizzes/{quizId}/take")
    @ResponseStatus(HttpStatus.CREATED)
    public Long takeQuiz(
            @PathVariable Long quizId,
            @RequestBody @Valid QuizDtos.TakeQuizRequest request
    ) {
        QuizSubmission submission = quizService.takeQuiz(quizId, request.studentId(), request.answers());
        return submission.getId();
    }

    @GetMapping("/quizzes/{quizId}/submissions")
    public List<Integer> resultsForQuiz(@PathVariable Long quizId) {
        return quizService.getSubmissionsForQuiz(quizId).stream()
                .map(QuizSubmission::getScore)
                .toList();
    }
}


