package hu.bme.aut.viauma06.language_learning.repository;

import hu.bme.aut.viauma06.language_learning.model.Course;
import hu.bme.aut.viauma06.language_learning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findCoursesByTeacher(User teacher);
    Optional<Course> findByIdAndTeacher(Integer id, User teacher);
    List<Course> findCoursesByStudents(User student);
    Optional<Course> findByIdAndStudents(Integer id, User student);
}
