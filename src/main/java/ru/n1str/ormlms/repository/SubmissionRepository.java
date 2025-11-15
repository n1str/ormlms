package ru.n1str.ormlms.repository;

import ru.n1str.ormlms.entity.Assignment;
import ru.n1str.ormlms.entity.Submission;
import ru.n1str.ormlms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByAssignment(Assignment assignment);

    List<Submission> findByStudent(User student);

    Optional<Submission> findByAssignmentAndStudent(Assignment assignment, User student);
}


