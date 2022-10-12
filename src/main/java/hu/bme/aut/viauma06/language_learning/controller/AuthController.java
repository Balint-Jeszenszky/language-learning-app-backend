package hu.bme.aut.viauma06.language_learning.controller;

import hu.bme.aut.viauma06.language_learning.model.dto.request.LoginRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.NewTokenRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.RegistrationRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.LoginResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.NewTokenResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.UserDetailsResponse;
import hu.bme.aut.viauma06.language_learning.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login/teacher")
    public ResponseEntity<LoginResponse> loginTeacher(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.loginTeacher(loginRequest);

        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/login/student")
    public ResponseEntity loginStudent() {
        return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED); // TODO
    }

    @PostMapping("/login/refresh")
    public ResponseEntity<NewTokenResponse> refresh(@RequestBody NewTokenRequest newTokenRequest) {
        NewTokenResponse newTokenResponse = authService.refreshLogin(newTokenRequest.getRefreshToken());

        return new ResponseEntity(newTokenResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetailsResponse> registerTeacher(@RequestBody RegistrationRequest registrationRequest) {
        UserDetailsResponse userDetailsResponse = authService.registerTeacher(registrationRequest);

        return new ResponseEntity(userDetailsResponse, HttpStatus.OK);
    }
}
