package hu.bme.aut.viauma06.language_learning.repository;

import hu.bme.aut.viauma06.language_learning.model.WordPair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordPairRepository extends JpaRepository<WordPair, Integer> {
    List<WordPair> findAllByCourseId(Integer id);
}
