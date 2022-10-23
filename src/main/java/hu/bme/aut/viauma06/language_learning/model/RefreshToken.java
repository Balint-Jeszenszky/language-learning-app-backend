package hu.bme.aut.viauma06.language_learning.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity(name = "RefreshToken")
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "expiration", nullable = false)
    private Date expiration;

    @ManyToOne
    private User user;

    public RefreshToken() {
    }

    public RefreshToken(String tokenHash, Date expiration, User user) {
        this.tokenHash = tokenHash;
        this.expiration = expiration;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(id, that.id) && Objects.equals(tokenHash, that.tokenHash) && Objects.equals(expiration, that.expiration) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tokenHash, expiration, user);
    }
}
