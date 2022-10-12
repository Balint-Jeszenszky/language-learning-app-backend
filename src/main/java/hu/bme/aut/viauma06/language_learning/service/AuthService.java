package hu.bme.aut.viauma06.language_learning.service;

import hu.bme.aut.viauma06.language_learning.controller.exceptions.BadRequestException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.InternalServerErrorException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.NotFoundException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.UnauthorizedException;
import hu.bme.aut.viauma06.language_learning.mapper.UserMapper;
import hu.bme.aut.viauma06.language_learning.model.ERole;
import hu.bme.aut.viauma06.language_learning.model.Role;
import hu.bme.aut.viauma06.language_learning.model.User;
import hu.bme.aut.viauma06.language_learning.model.dto.request.LoginRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.RegistrationRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.LoginResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.NewTokenResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.UserDetailsResponse;
import hu.bme.aut.viauma06.language_learning.repository.RoleRepository;
import hu.bme.aut.viauma06.language_learning.repository.UserRepository;
import hu.bme.aut.viauma06.language_learning.security.jwt.JwtUtils;
import hu.bme.aut.viauma06.language_learning.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse loginTeacher(LoginRequest loginRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail().toLowerCase(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("User not found");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails =  (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        String refreshJwt = jwtUtils.generateJwtRefreshToken(userDetails);

        User user = userRepository.findById(((UserDetailsImpl) authentication.getPrincipal()).getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserDetails(UserMapper.INSTANCE.userToUserDetailsResponse(user));
        loginResponse.setAccessToken(jwt);
        loginResponse.setRefreshToken(refreshJwt);

        return loginResponse;
    }

    public NewTokenResponse refreshLogin(String refreshToken) {
        if (!jwtUtils.validateJwtRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Wrong refresh token");
        }

        String email = jwtUtils.getEmailFromJwtRefreshToken(refreshToken);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("User not found"));

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        String jwt = jwtUtils.generateJwtToken(userDetails);
        String refreshJwt = jwtUtils.generateJwtRefreshToken(userDetails);

        NewTokenResponse newTokenResponse = new NewTokenResponse();
        newTokenResponse.setAccessToken(jwt);
        newTokenResponse.setRefreshToken(refreshJwt);

        return newTokenResponse;
    }

    public UserDetailsResponse registerTeacher(RegistrationRequest registrationRequest) {
        String email = registrationRequest.getEmail().toLowerCase();
        validateEmail(email);

        Optional<Role> role = roleRepository.findByName(ERole.ROLE_TEACHER);

        if (role.isEmpty()) {
            createDefaultRoles();
            role = roleRepository.findByName(ERole.ROLE_TEACHER);
        }

        if (userRepository.existsByEmailAndRolesIn(email, Set.of(role.get()))) {
            throw new BadRequestException("Error: Email is already in use!");
        }

        if (registrationRequest.getPassword().length() < 8) {
            throw new BadRequestException("Error: Password should be at least 8 character!");
        }

        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            throw new BadRequestException("Error: Passwords not match!");
        }

        User user = new User(
                registrationRequest.getName(),
                email,
                passwordEncoder.encode(registrationRequest.getPassword())
        );

        Set<Role> roles = new HashSet<>();

        roles.add(roleRepository.findByName(ERole.ROLE_TEACHER)
                .orElseThrow(() -> new InternalServerErrorException("Error: Role is not found.")));

        user.setRoles(roles);

        userRepository.save(user);

        UserDetailsResponse userDetailsResponse = UserMapper.INSTANCE.userToUserDetailsResponse(user);

        return userDetailsResponse;
    }

    private void validateEmail(String email) {
        Pattern emailPattern = Pattern.compile("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
        Matcher emailMatcher = emailPattern.matcher(email);

        if (!emailMatcher.matches()) {
            throw new BadRequestException("Error: Email is invalid!");
        }
    }

    private void createDefaultRoles() {
        roleRepository.save(new Role(ERole.ROLE_TEACHER));
        roleRepository.save(new Role(ERole.ROLE_STUDENT));
    }
}
