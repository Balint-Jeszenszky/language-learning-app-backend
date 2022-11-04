package hu.bme.aut.viauma06.language_learning.service;

import hu.bme.aut.viauma06.language_learning.controller.exceptions.BadRequestException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.InternalServerErrorException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.NotFoundException;
import hu.bme.aut.viauma06.language_learning.controller.exceptions.UnauthorizedException;
import hu.bme.aut.viauma06.language_learning.mapper.UserMapper;
import hu.bme.aut.viauma06.language_learning.model.ERole;
import hu.bme.aut.viauma06.language_learning.model.RefreshToken;
import hu.bme.aut.viauma06.language_learning.model.Role;
import hu.bme.aut.viauma06.language_learning.model.User;
import hu.bme.aut.viauma06.language_learning.model.dto.request.LoginRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.request.RegistrationRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.LoginResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.NewTokenResponse;
import hu.bme.aut.viauma06.language_learning.model.dto.response.UserDetailsResponse;
import hu.bme.aut.viauma06.language_learning.repository.RefreshTokenRepository;
import hu.bme.aut.viauma06.language_learning.repository.RoleRepository;
import hu.bme.aut.viauma06.language_learning.repository.UserRepository;
import hu.bme.aut.viauma06.language_learning.security.jwt.JwtUtils;
import hu.bme.aut.viauma06.language_learning.security.service.UserDetailsImpl;
import hu.bme.aut.viauma06.language_learning.service.util.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@EnableScheduling
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
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

        saveRefreshToken(refreshJwt, user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserDetails(UserMapper.INSTANCE.userToUserDetailsResponse(user));
        loginResponse.setAccessToken(jwt);
        loginResponse.setRefreshToken(refreshJwt);

        return loginResponse;
    }

    @Transactional
    public NewTokenResponse refreshLogin(String refreshToken) {
        if (!jwtUtils.validateJwtRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Wrong refresh token");
        }

        String hashOfJwtToken = getHashOfJwtToken(refreshToken);
        Optional<RefreshToken> storedToken = refreshTokenRepository.findFirstByTokenHash(hashOfJwtToken);

        if (storedToken.isEmpty()) {
            throw new UnauthorizedException("Wrong refresh token");
        }

        refreshTokenRepository.deleteAllByTokenHash(storedToken.get().getTokenHash());

        String email = jwtUtils.getEmailFromJwtRefreshToken(refreshToken);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("User not found"));

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        String jwt = jwtUtils.generateJwtToken(userDetails);
        String refreshJwt = jwtUtils.generateJwtRefreshToken(userDetails);

        NewTokenResponse newTokenResponse = new NewTokenResponse();
        newTokenResponse.setAccessToken(jwt);
        newTokenResponse.setRefreshToken(refreshJwt);

        saveRefreshToken(refreshJwt, user);

        return newTokenResponse;
    }

    public UserDetailsResponse registerTeacher(RegistrationRequest registrationRequest) {
        String email = registrationRequest.getEmail().toLowerCase();

        if (registrationRequest.getName().length() < 3) {
            throw new BadRequestException("Error: Name should be at least 3 characters long!");
        }

        if (!EmailValidator.validateEmail(email)) {
            throw new BadRequestException("Error: Email is invalid!");
        }

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

        Set<Role> roles = new HashSet<>();

        roles.add(roleRepository.findByName(ERole.ROLE_TEACHER)
                .orElseThrow(() -> new InternalServerErrorException("Error: Role is not found.")));

        User user = new User(
                registrationRequest.getName(),
                email,
                passwordEncoder.encode(registrationRequest.getPassword()),
                roles
        );

        user.setRoles(roles);

        userRepository.save(user);

        UserDetailsResponse userDetailsResponse = UserMapper.INSTANCE.userToUserDetailsResponse(user);

        return userDetailsResponse;
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteAllByTokenHash(getHashOfJwtToken(refreshToken));
    }

    private void createDefaultRoles() {
        roleRepository.save(new Role(ERole.ROLE_TEACHER));
        roleRepository.save(new Role(ERole.ROLE_STUDENT));
    }

    private void saveRefreshToken(String token, User user) {
        String tokenHash = getHashOfJwtToken(token);
        Date expiration = jwtUtils.getRefreshTokenExpiration(token);
        refreshTokenRepository.save(new RefreshToken(tokenHash, expiration, user));
    }

    private String getHashOfJwtToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return String.copyValueOf(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Scheduled(cron = "0 3 * * * ?", zone = "Europe/Budapest")
    protected void clearExpiredRefreshTokens() {
        List<RefreshToken> expired = refreshTokenRepository.findByExpirationLessThan(new Date());
        refreshTokenRepository.deleteAllInBatch(expired);
    }
}
