package ru.n1str.ormlms.api;

import ru.n1str.ormlms.api.dto.CourseDtos;
import ru.n1str.ormlms.entity.Course;
import ru.n1str.ormlms.entity.Lesson;
import ru.n1str.ormlms.entity.Module;
import ru.n1str.ormlms.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDtos.CourseResponse create(@RequestBody @Valid CourseDtos.CreateCourseRequest request) {
        Course course = courseService.createCourse(
                request.title(),
                request.description(),
                request.categoryId(),
                request.teacherId(),
                request.durationHours(),
                request.startDate(),
                request.tagIds()
        );
        return new CourseDtos.CourseResponse(course.getId(), course.getTitle(), course.getDescription());
    }

    @GetMapping("/{id}")
    public CourseDtos.CourseResponse get(@PathVariable Long id) {
        Course course = courseService.getCourse(id);
        return new CourseDtos.CourseResponse(course.getId(), course.getTitle(), course.getDescription());
    }

    @GetMapping("/{id}/details")
    public CourseDtos.CourseDetailsResponse details(@PathVariable Long id) {
        Course course = courseService.getCourseWithStructure(id);
        List<CourseDtos.CourseDetailsResponse.ModuleSummary> modules = course.getModules().stream()
                .map(this::mapModule)
                .toList();
        return new CourseDtos.CourseDetailsResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                modules
        );
    }

    @PostMapping("/{courseId}/modules")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDtos.CourseDetailsResponse.ModuleSummary addModule(
            @PathVariable Long courseId,
            @RequestBody @Valid CourseDtos.CreateModuleRequest request
    ) {
        Module module = courseService.addModule(courseId, request.title(), request.orderIndex(), request.description());
        return new CourseDtos.CourseDetailsResponse.ModuleSummary(
                module.getId(),
                module.getTitle(),
                module.getOrderIndex(),
                List.of()
        );
    }

    @PostMapping("/modules/{moduleId}/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDtos.CourseDetailsResponse.LessonSummary addLesson(
            @PathVariable Long moduleId,
            @RequestBody @Valid CourseDtos.CreateLessonRequest request
    ) {
        Lesson lesson = courseService.addLesson(moduleId, request.title(), request.content(), request.videoUrl());
        return new CourseDtos.CourseDetailsResponse.LessonSummary(lesson.getId(), lesson.getTitle());
    }

    private CourseDtos.CourseDetailsResponse.ModuleSummary mapModule(Module module) {
        List<CourseDtos.CourseDetailsResponse.LessonSummary> lessons = module.getLessons().stream()
                .map(l -> new CourseDtos.CourseDetailsResponse.LessonSummary(l.getId(), l.getTitle()))
                .toList();
        return new CourseDtos.CourseDetailsResponse.ModuleSummary(
                module.getId(),
                module.getTitle(),
                module.getOrderIndex(),
                lessons
        );
    }
}


