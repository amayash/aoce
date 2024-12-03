package com.spring.aoce.service;

import com.spring.aoce.dto.UserSignUpDto;
import com.spring.aoce.entity.User;
import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private static UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository repository) {
        UserService.userRepository = repository;
    }

    public static User findByEmail(String email) {
        return userRepository.findOneByEmailIgnoreCase(email);
    }

    @Transactional
    public User create(UserSignUpDto userSignupDto) {
        final User user = findByEmail(userSignupDto.getEmail());
        if (user != null) {
            throw new ValidationException(String.format("User '%s' already exists", user.getEmail()));
        }
        if (userRepository.findUserByPhone(userSignupDto.getPhone()) != null) {
            throw new ValidationException(String.format("User with phone number '%s' already exists", userSignupDto.getPhone()));
        }
        if (!Objects.equals(userSignupDto.getPassword(), userSignupDto.getPasswordConfirm())) {
            throw new ValidationException("Passwords not equals");
        }

        final User newUser = new User(null, userSignupDto.getEmail(), passwordEncoder.encode(userSignupDto.getPassword()),
                getRole(userSignupDto.getCode()), userSignupDto.getFullName(), userSignupDto.getPhone(), userSignupDto.getBirthday(),
                new ArrayList<>());
        return userRepository.save(newUser);
    }

    private UserRole getRole(String code) {
        return Strings.isBlank(code) || !Objects.equals(code, "111") ? UserRole.USER : UserRole.ADMIN;
    }

    public static User findByPrincipal() {
        User user = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            user = findByEmail(userDetails.getUsername());
        }
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User userEntity = findByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(), userEntity.getPassword(), Collections.singleton(userEntity.getRole()));
    }
}
