package hu.bme.aut.viauma06.language_learning.repository;

import hu.bme.aut.viauma06.language_learning.model.Course;
import hu.bme.aut.viauma06.language_learning.model.CourseStudentAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseStudentAccessRepository extends JpaRepository<CourseStudentAccess, Integer> {
    List<CourseStudentAccess> findAllByCourse(Course course);
}
