package hu.bme.aut.viauma06.language_learning.repository;

import hu.bme.aut.viauma06.language_learning.model.CourseStudentAccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseStudentAccessRepository extends JpaRepository<CourseStudentAccess, Integer> {
}
