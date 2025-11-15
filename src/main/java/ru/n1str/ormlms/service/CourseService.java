package ru.n1str.ormlms.service;

import ru.n1str.ormlms.entity.*;
import ru.n1str.ormlms.exception.NotFoundException;
import ru.n1str.ormlms.model.UserRole;
import ru.n1str.ormlms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
    }

    public Course getCourseWithStructure(Long id) {
        return courseRepository.findWithStructureById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
    }

    public List<Course> findByCategory(String categoryName) {
        return courseRepository.findByCategory_Name(categoryName);
    }

    @Transactional
    public Course createCourse(
            String title,
            String description,
            Long categoryId,
            Long teacherId,
            Integer durationHours,
            LocalDate startDate,
            Set<Long> tagIds
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found: " + categoryId));
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + teacherId));
        if (teacher.getRole() != UserRole.TEACHER && teacher.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User is not a teacher: " + teacherId);
        }

        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDurationHours(durationHours);
        course.setStartDate(startDate);

        if (tagIds != null && !tagIds.isEmpty()) {
            Set<Tag> tags = tagIds.stream()
                    .map(id -> tagRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Tag not found: " + id)))
                    .collect(Collectors.toSet());
            course.setTags(tags);
        }

        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(Long id, String title, String description, Integer durationHours) {
        Course course = getCourse(id);
        if (title != null) {
            course.setTitle(title);
        }
        if (description != null) {
            course.setDescription(description);
        }
        if (durationHours != null) {
            course.setDurationHours(durationHours);
        }
        return course;
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourse(id);
        courseRepository.delete(course);
    }

    @Transactional
    public Module addModule(Long courseId, String title, Integer orderIndex, String description) {
        Course course = getCourse(courseId);
        Module module = new Module();
        module.setCourse(course);
        module.setTitle(title);
        module.setOrderIndex(orderIndex);
        module.setDescription(description);
        return moduleRepository.save(module);
    }

    @Transactional
    public Lesson addLesson(Long moduleId, String title, String content, String videoUrl) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Module not found: " + moduleId));
        Lesson lesson = new Lesson();
        lesson.setModule(module);
        lesson.setTitle(title);
        lesson.setContent(content);
        lesson.setVideoUrl(videoUrl);
        return lessonRepository.save(lesson);
    }
}


