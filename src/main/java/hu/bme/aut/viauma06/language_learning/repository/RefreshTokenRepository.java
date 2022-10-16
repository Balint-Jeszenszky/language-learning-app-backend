package hu.bme.aut.viauma06.language_learning.repository;

import hu.bme.aut.viauma06.language_learning.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    List<RefreshToken> findByExpirationLessThan(Date expiration);
}
