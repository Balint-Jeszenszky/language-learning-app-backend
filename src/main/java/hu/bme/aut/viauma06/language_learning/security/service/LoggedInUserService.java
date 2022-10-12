package hu.bme.aut.viauma06.language_learning.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

import static hu.bme.aut.viauma06.language_learning.model.ERole.ROLE_TEACHER;

@Service
public class LoggedInUserService {

    public UserDetailsImpl getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetailsImpl) authentication.getPrincipal());
    }

    public boolean isTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().map(a -> a.getAuthority()).collect(Collectors.toList()).contains(ROLE_TEACHER.name());
    }
}
