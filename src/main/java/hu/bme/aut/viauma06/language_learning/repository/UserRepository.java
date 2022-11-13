package hu.bme.aut.viauma06.language_learning.repository;

import hu.bme.aut.viauma06.language_learning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findAllByEmailIn(List<String> emails);
    Boolean existsByEmail(String email);
}
