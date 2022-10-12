package hu.bme.aut.viauma06.language_learning.model.dto.request;

public class NewTokenRequest {
    private String refreshToken;

    public NewTokenRequest() {
    }

    public NewTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
