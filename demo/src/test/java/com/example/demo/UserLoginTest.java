package com.example.demo;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserLoginTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateUser_ValidCredentials() throws Exception {
        String usernameOrEmail = "testuser";
        String password = "password123";

        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setEmail("testuser@example.com");
        mockUser.setPassword_hash(BCrypt.hashpw(password, BCrypt.gensalt()));

        when(userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail))
                .thenReturn(Optional.of(mockUser));

        User result = userService.authenticateUser(usernameOrEmail, password);

        assertNotNull(result);
        assertEquals(mockUser.getUsername(), result.getUsername());
        assertEquals(mockUser.getEmail(), result.getEmail());

        verify(userRepository, times(1)).findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    @Test
    public void testAuthenticateUser_InvalidUsernameOrEmail() {
        String usernameOrEmail = "nonexistentuser";
        String password = "password123";

        when(userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () ->
                userService.authenticateUser(usernameOrEmail, password));

        assertEquals("Invalid username/email or password", exception.getMessage());

        verify(userRepository, times(1)).findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    @Test
    public void testAuthenticateUser_InvalidPassword() {
        String usernameOrEmail = "testuser";
        String correctPassword = "password123";
        String wrongPassword = "wrongpassword";

        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setEmail("testuser@example.com");
        mockUser.setPassword_hash(BCrypt.hashpw(correctPassword, BCrypt.gensalt()));

        when(userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail))
                .thenReturn(Optional.of(mockUser));

        Exception exception = assertThrows(Exception.class, () ->
                userService.authenticateUser(usernameOrEmail, wrongPassword));

        assertEquals("Invalid username/email or password", exception.getMessage());

        verify(userRepository, times(1)).findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    @Test
    public void testRegisterUser_SuccessfulRegistration() throws Exception {
        // Arrange
        String username = "newuser";
        String email = "newuser@example.com";
        String password = "securePassword";

        when(userRepository.findByUsernameOrEmail(username, email))
                .thenReturn(Optional.empty());

        User mockUser = new User(username, email, BCrypt.hashpw(password, BCrypt.gensalt()));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        User result = userService.registerUser(username, email, password);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertTrue(BCrypt.checkpw(password, result.getPassword_hash()));

        verify(userRepository, times(1)).findByUsernameOrEmail(username, email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameOrEmailTaken() {
        String username = "existinguser";
        String email = "existinguser@example.com";
        String password = "password123";

        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setEmail(email);

        when(userRepository.findByUsernameOrEmail(username, email))
                .thenReturn(Optional.of(existingUser));

        Exception exception = assertThrows(Exception.class, () ->
                userService.registerUser(username, email, password));

        assertEquals("Username or email is already taken", exception.getMessage());

        verify(userRepository, times(1)).findByUsernameOrEmail(username, email);
        verify(userRepository, never()).save(any(User.class));
    }
}

