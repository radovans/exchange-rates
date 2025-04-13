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
import org.springframework.data.domain.Sort;

import java.util.List;
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

    @Test
    void findAllUsersReturnsSortedUsers() {
        final Sort sort = Sort.by("id");
        final List<User> mockUsers = List.of(
                createUserAlice(),
                createUserBob()
        );
        when(userRepository.findAll(sort)).thenReturn(mockUsers);

        final List<User> result = userService.find(sort);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Alice", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Bob", result.get(1).getName());
        verify(userRepository, times(1)).findAll(sort);
    }

    @Test
    void createUserSavesAndReturnsUser() {
        final User newUser = createUserAliceWithoutId();
        final User savedUser = createUserAlice();
        when(userRepository.save(newUser)).thenReturn(savedUser);

        final User result = userService.createUser(newUser);

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getName(), result.getName());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void updateUserUpdatesAndReturnsUserWhenUserExists() throws ResourceNotFoundException {
        final User existingUser = createUserAlice();
        final User updatedUser = createUserAliceWithoutId();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        final User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals(existingUser.getId(), result.getId());
        assertEquals(existingUser.getName(), result.getName());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUserThrowsExceptionWhenUserDoesNotExist() {
        final User updatedUser = createUserAliceWithoutId();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, updatedUser));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUserWhenUserExists() throws ResourceNotFoundException {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUserThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).deleteById(anyLong());
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

    private static User createUserAliceWithoutId() {
        return User.builder()
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
