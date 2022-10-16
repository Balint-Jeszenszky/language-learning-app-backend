package hu.bme.aut.viauma06.language_learning.model.dto.request;

public class RefreshTokenRequest {
    private String refreshToken;

    public RefreshTokenRequest() {
    }

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
