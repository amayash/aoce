package com.spring.aoce.service;

import com.spring.aoce.dto.UserSignUpDto;
import com.spring.aoce.entity.User;
import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService.setUserRepository(userRepository);
    }

    @Test
    void testCreate_UserAlreadyExists_ThrowsValidationException() {
        // Arrange
        UserSignUpDto userSignUpDto = new UserSignUpDto();
        userSignUpDto.setEmail("test@example.com");

        when(userRepository.findOneByEmailIgnoreCase("test@example.com")).thenReturn(new User());

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.create(userSignUpDto));
        assertEquals("User 'null' already exists", exception.getMessage());
    }

    @Test
    void testCreate_UserWithPhoneExists_ThrowsValidationException() {
        // Arrange
        UserSignUpDto userSignUpDto = new UserSignUpDto();
        userSignUpDto.setEmail("test@example.com");
        userSignUpDto.setPhone("123456789");

        when(userRepository.findOneByEmailIgnoreCase("test@example.com")).thenReturn(null);
        when(userRepository.findUserByPhone("123456789")).thenReturn(new User());

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.create(userSignUpDto));
        assertEquals("User with phone number '123456789' already exists", exception.getMessage());
    }

    @Test
    void testCreate_PasswordsNotMatching_ThrowsValidationException() {
        // Arrange
        UserSignUpDto userSignUpDto = new UserSignUpDto();
        userSignUpDto.setEmail("test@example.com");
        userSignUpDto.setPassword("password");
        userSignUpDto.setPasswordConfirm("differentPassword");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.create(userSignUpDto));
        assertEquals("Passwords not equals", exception.getMessage());
    }

    @Test
    void testCreate_Success() {
        // Arrange
        UserSignUpDto userSignUpDto = new UserSignUpDto();
        userSignUpDto.setEmail("test@example.com");
        userSignUpDto.setPassword("password");
        userSignUpDto.setPasswordConfirm("password");
        userSignUpDto.setCode("111");

        when(userRepository.findOneByEmailIgnoreCase("test@example.com")).thenReturn(null);
        when(userRepository.findUserByPhone(anyString())).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User createdUser = userService.create(userSignUpDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(UserRole.ADMIN, createdUser.getRole());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    void testFindByPrincipal_UserNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        SecurityContextHolder.clearContext();

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, UserService::findByPrincipal);
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userRepository.findOneByEmailIgnoreCase("unknown@example.com")).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("unknown@example.com"));
        assertEquals("unknown@example.com", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);

        when(userRepository.findOneByEmailIgnoreCase("test@example.com")).thenReturn(user);

        // Act
        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) userService.loadUserByUsername("test@example.com");

        // Assert
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
    }
}
