package hu.bme.aut.viauma06.language_learning.repository;

import hu.bme.aut.viauma06.language_learning.model.Course;
import hu.bme.aut.viauma06.language_learning.model.WordPair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WordPairRepository extends JpaRepository<WordPair, Integer> {
    List<WordPair> findAllByCourseId(Integer id);

    Optional<WordPair> findByCourseInAndId(List<Course> courses, Integer id);
}
