package hu.bme.aut.viauma06.language_learning.model.dto.response;

public class NewTokenResponse {
    private String accessToken;
    private String refreshToken;

    public NewTokenResponse() {
    }

    public NewTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
