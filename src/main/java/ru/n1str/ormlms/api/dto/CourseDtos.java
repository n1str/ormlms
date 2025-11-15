package ru.n1str.ormlms.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class CourseDtos {

    public record CreateCourseRequest(
            @NotBlank String title,
            String description,
            @NotNull Long categoryId,
            @NotNull Long teacherId,
            Integer durationHours,
            LocalDate startDate,
            Set<Long> tagIds
    ) {
    }

    public record CourseResponse(
            Long id,
            String title,
            String description
    ) {
    }

    public record CourseDetailsResponse(
            Long id,
            String title,
            String description,
            List<ModuleSummary> modules
    ) {
        public record ModuleSummary(
                Long id,
                String title,
                Integer orderIndex,
                List<LessonSummary> lessons
        ) {
        }

        public record LessonSummary(
                Long id,
                String title
        ) {
        }
    }

    public record CreateModuleRequest(
            @NotBlank String title,
            Integer orderIndex,
            String description
    ) {
    }

    public record CreateLessonRequest(
            @NotBlank String title,
            String content,
            String videoUrl
    ) {
    }
}


