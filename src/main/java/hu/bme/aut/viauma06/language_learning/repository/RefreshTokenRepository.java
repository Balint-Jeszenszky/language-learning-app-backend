package hu.bme.aut.viauma06.language_learning.repository;

import hu.bme.aut.viauma06.language_learning.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findFirstByTokenHash(String tokenHash);
    List<RefreshToken> findByExpirationLessThan(Date expiration);
    @Modifying
    @Query("delete from RefreshToken r where r.tokenHash = :tokenHash")
    void deleteAllByTokenHash(String tokenHash);
}
