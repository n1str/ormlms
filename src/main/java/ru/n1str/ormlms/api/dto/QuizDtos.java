package ru.n1str.ormlms.api.dto;

import ru.n1str.ormlms.model.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class QuizDtos {

    public record CreateQuizRequest(
            @NotBlank String title,
            Integer timeLimitMinutes
    ) {
    }

    public record CreateQuestionRequest(
            @NotBlank String text,
            @NotNull QuestionType type
    ) {
    }

    public record CreateAnswerOptionRequest(
            @NotBlank String text,
            boolean correct
    ) {
    }

    public record TakeQuizRequest(
            @NotNull Long studentId,
            @NotNull Map<Long, List<Long>> answers
    ) {
    }
}


