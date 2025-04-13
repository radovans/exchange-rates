package cz.sinko.exchangerates.service.impl;

import cz.sinko.exchangerates.repository.UserRepository;
import cz.sinko.exchangerates.repository.entity.Role;
import cz.sinko.exchangerates.repository.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static cz.sinko.exchangerates.configuration.Constants.USER_ROLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AuthUserServiceImpl}
 *
 * @author Radovan Šinko
 */
class AuthUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthUserServiceImpl authUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserExists() {
        final User user = createUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        final User result = (User) authUserService.loadUserByUsername(user.getUsername());

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getAuthorities(), result.getAuthorities());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void loadUserByUsernameThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        final String expectedMessage = "User with username 'nonexistent' not found";
        final UsernameNotFoundException actualException = assertThrows(UsernameNotFoundException.class, () -> {
            authUserService.loadUserByUsername("nonexistent");
        });

        assertEquals(expectedMessage, actualException.getMessage());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    private static Role createUserRole() {
        return Role.builder().id(1L).authority(USER_ROLE).build();
    }

    private static User createUser() {
        return User.builder()
                .id(2L)
                .name("Bob")
                .username("bob")
                .password("Password123")
                .authorities(Set.of(createUserRole()))
                .build();
    }
}
