package ru.n1str.ormlms;

import ru.n1str.ormlms.entity.Course;
import ru.n1str.ormlms.repository.CourseRepository;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class LazyLoadingTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void accessingLazyCollectionOutsideSession_throwsLazyInitializationException() {
        Course course = new Course();
        course.setTitle("Lazy course");
        courseRepository.save(course);

        Course detached = courseRepository.findById(course.getId()).orElseThrow();

        assertThatThrownBy(() -> detached.getModules().size())
                .isInstanceOf(LazyInitializationException.class);
    }
}


