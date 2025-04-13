package cz.sinko.exchangerates.service.impl;

import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.repository.UserRepository;
import cz.sinko.exchangerates.repository.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UserServiceImpl}
 *
 * @author Radovan Šinko
 */
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("provideUserData")
    void findUserByIdReturnsUserWhenUserExists(final User user) throws ResourceNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        final User result = userService.find(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findUserByIdThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.find(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    private static Stream<Arguments> provideUserData() {
        return Stream.of(
                Arguments.of(createUserAlice()),
                Arguments.of(createUserBob())
        );
    }

    private static User createUserAlice() {
        return User.builder()
                .id(1L)
                .name("Alice")
                .build();
    }

    private static User createUserBob() {
        return User.builder()
                .id(2L)
                .name("Bob")
                .build();
    }
}
