package hu.bme.aut.viauma06.language_learning.model.dto.response;

public class LoginResponse {
    private UserDetailsResponse userDetails;
    private String accessToken;
    private String refreshToken;

    public LoginResponse() {
    }

    public LoginResponse(UserDetailsResponse userDetails, String accessToken, String refreshToken) {
        this.userDetails = userDetails;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public UserDetailsResponse getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsResponse userDetails) {
        this.userDetails = userDetails;
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
