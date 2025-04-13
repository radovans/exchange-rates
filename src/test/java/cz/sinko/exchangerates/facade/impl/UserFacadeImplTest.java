package cz.sinko.exchangerates.facade.impl;

import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.repository.entity.User;
import cz.sinko.exchangerates.service.UserService;
import cz.sinko.exchangerates.service.dto.UserDto;
import cz.sinko.exchangerates.service.mapper.UserMapper;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UserFacadeImpl}
 *
 * @author Radovan Šinko
 */
class UserFacadeImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserFacadeImpl userFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("provideUserData")
    void getUserWhenUserExists(final User user, final UserDto expectedUserDto) throws ResourceNotFoundException {
        when(userService.find(1L)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(expectedUserDto);

        final UserDto result = userFacade.getUser(1L);

        assertNotNull(result);
        assertEquals(expectedUserDto.getId(), result.getId());
        assertEquals(expectedUserDto.getName(), result.getName());
        verify(userService, times(1)).find(1L);
    }

    @Test
    void getUserThrowsExceptionWhenUserDoesNotExist() throws ResourceNotFoundException {
        when(userService.find(1L)).thenThrow(ResourceNotFoundException
                .createWith("User", "with id '1L' was not found"));

        assertThrows(ResourceNotFoundException.class, () -> userFacade.getUser(1L));
        verify(userService, times(1)).find(1L);
    }

    @Test
    void getUsersReturnsListOfUserDtos() {
        final List<User> users = List.of(
                createUserAlice(),
                createUserBob()
        );
        when(userService.find(Sort.by("id").ascending())).thenReturn(users);
        when(userMapper.toUsers(users)).thenReturn(List.of(
                createUserDtoAlice(),
                createUserDtoBob()
        ));

        final List<UserDto> result = userFacade.getUsers();
        final UserDto userAlice = result.get(0);
        final UserDto userBob = result.get(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, userAlice.getId());
        assertEquals("Alice", userAlice.getName());
        assertEquals(2L, userBob.getId());
        assertEquals("Bob", userBob.getName());
        verify(userService, times(1)).find(Sort.by("id").ascending());
    }

    @ParameterizedTest
    @MethodSource("provideUserCreationData")
    void createUserCreatesUserSuccessfully(final UserDto userDto, final User user) {
        when(userService.createUser(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        final UserDto result = userFacade.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getName(), result.getName());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void updateUserUpdatesAndReturnsUserWhenUserExists() throws ResourceNotFoundException {
        final long userId = 1L;
        final UserDto userDto = createUserDtoAlice();
        final User userAliceWithoutId = createUserAliceWithoutId();
        final User userAliceWithId = createUserAlice();

        when(userService.updateUser(userId, userAliceWithoutId)).thenReturn(userAliceWithId);
        when(userMapper.toUserDto(userAliceWithId)).thenReturn(userDto);

        final UserDto result = userFacade.updateUser(userId, userDto);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        verify(userService, times(1)).updateUser(userId, userAliceWithoutId);
    }

    @Test
    void deleteUserWhenUserExists() throws ResourceNotFoundException {
        final long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        userFacade.deleteUser(userId);

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void deleteUserThrowsExceptionWhenUserDoesNotExist() throws ResourceNotFoundException {
        final long userId = 1L;

        doThrow(ResourceNotFoundException.createWith("User", "with id '1L' was not found"))
                .when(userService).deleteUser(userId);

        assertThrows(ResourceNotFoundException.class, () -> userFacade.deleteUser(userId));
        verify(userService, times(1)).deleteUser(userId);
    }

    private static Stream<Arguments> provideUserData() {
        return Stream.of(
                Arguments.of(createUserAlice(), createUserDtoAlice()),
                Arguments.of(createUserBob(), createUserDtoBob())
        );
    }

    private static Stream<Arguments> provideUserCreationData() {
        return Stream.of(
                Arguments.of(createUserDtoAlice(), createUserAliceWithoutId()),
                Arguments.of(createUserDtoBob(), createUserBobWithoutId())
        );
    }

    private static User createUserAlice() {
        return User.builder()
                .id(1L)
                .name("Alice")
                .build();
    }

    private static UserDto createUserDtoAlice() {
        return UserDto.builder()
                .id(1L)
                .name("Alice")
                .build();
    }

    private static User createUserAliceWithoutId() {
        return User.builder()
                .name("Alice")
                .build();
    }

    private static User createUserBobWithoutId() {
        return User.builder()
                .name("Bob")
                .build();
    }

    private static User createUserBob() {
        return User.builder()
                .id(2L)
                .name("Bob")
                .build();
    }

    private static UserDto createUserDtoBob() {
        return UserDto.builder()
                .id(2L)
                .name("Bob")
                .build();
    }
}
