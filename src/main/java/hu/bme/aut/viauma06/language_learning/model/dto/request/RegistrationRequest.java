package hu.bme.aut.viauma06.language_learning.model.dto.request;

public class RegistrationRequest {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private Boolean hasStudentAccount;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String name, String email, String password, String confirmPassword, Boolean hasStudentAccount) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.hasStudentAccount = hasStudentAccount;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public Boolean getHasStudentAccount() {
        return hasStudentAccount;
    }
}
