package cz.sinko.exchangerates.facade.impl;

import cz.sinko.exchangerates.configuration.exception.AlreadyExistsException;
import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.repository.entity.Role;
import cz.sinko.exchangerates.repository.entity.User;
import cz.sinko.exchangerates.service.RoleService;
import cz.sinko.exchangerates.service.UserService;
import cz.sinko.exchangerates.service.dto.user.UserDto;
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
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static cz.sinko.exchangerates.configuration.Constants.ADMIN_ROLE;
import static cz.sinko.exchangerates.configuration.Constants.USER_ROLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UserFacadeImpl}
 *
 * @author Radovan Šinko
 */
class UserFacadeImplTest {

    @Mock
    private RoleService roleService;

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
        assertEquals(expectedUserDto.getUsername(), result.getUsername());
        assertEquals(expectedUserDto.isAdmin(), result.isAdmin());
        assertEquals(expectedUserDto.getPassword(), result.getPassword());
        verify(userService, times(1)).find(1L);
    }

    @Test
    void getUserThrowsExceptionWhenUserDoesNotExist() throws ResourceNotFoundException {
        when(userService.find(1L)).thenThrow(ResourceNotFoundException
                .createWith("User", "with id '1' was not found"));

        final String expectedMessage = "User with id '1' was not found";
        final ResourceNotFoundException actualException = assertThrows(ResourceNotFoundException.class, () -> {
            userFacade.getUser(1L);
        });

        assertEquals(expectedMessage, actualException.getMessage());
        verify(userService, times(1)).find(1L);
    }

    @Test
    void getUsersReturnsListOfUserDtos() {
        final List<User> users = List.of(
                createAdmin(),
                createUser()
        );
        when(userService.find(Sort.by("id").ascending())).thenReturn(users);
        when(userMapper.toUsers(users)).thenReturn(List.of(
                createAdminDto(),
                createUserDto()
        ));

        final List<UserDto> result = userFacade.getUsers();
        final UserDto userAlice = result.get(0);
        final UserDto userBob = result.get(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, userAlice.getId());
        assertEquals("Alice", userAlice.getName());
        assertEquals("alice", userAlice.getUsername());
        assertEquals("Password123", userAlice.getPassword());
        assertTrue(userAlice.isAdmin());
        assertEquals(2L, userBob.getId());
        assertEquals("Bob", userBob.getName());
        assertEquals("bob", userBob.getUsername());
        assertEquals("Password123", userBob.getPassword());
        assertFalse(userBob.isAdmin());
        verify(userService, times(1)).find(Sort.by("id").ascending());
    }

    @ParameterizedTest
    @MethodSource("provideUserCreationData")
    void createUserCreatesUserSuccessfully(final UserDto userDto,
                                           final User user,
                                           final User createdUser,
                                           final UserDto createdUserDto) throws AlreadyExistsException {

        when(userService.createUser(user)).thenReturn(createdUser);
        when(userMapper.toUserDto(createdUser)).thenReturn(createdUserDto);
        when(roleService.findByAuthority(USER_ROLE)).thenReturn(createUserRole());
        when(roleService.findByAuthority(ADMIN_ROLE)).thenReturn(createAdminRole());

        final UserDto result = userFacade.createUser(userDto);

        assertNotNull(result);
        assertEquals(createdUserDto.getId(), result.getId());
        assertEquals(createdUserDto.getName(), result.getName());
        assertEquals(createdUserDto.getUsername(), result.getUsername());
        assertEquals(createdUserDto.getPassword(), result.getPassword());
        assertEquals(createdUserDto.isAdmin(), result.isAdmin());
        verify(userService, times(1)).createUser(user);
        verify(userMapper, times(1)).toUserDto(createdUser);
        verify(roleService, times(1)).findByAuthority(any());
    }

    @Test
    void updateUserAccessDeniedWhenUserIsNotOwner() throws ResourceNotFoundException {
        final User loggedUser = createUser();
        final long userId = 1L;
        final UserDto userDto = createAdminDto();

        final String expectedMessage = "User is not allowed to update other users";
        final AccessDeniedException actualException = assertThrows(AccessDeniedException.class, () -> {
            userFacade.updateUser(loggedUser, userId, userDto);
        });

        assertEquals(expectedMessage, actualException.getMessage());
        verify(userService, never()).updateUser(anyLong(), any());
    }

    @Test
    void updateUserAsUserSuccessfully() throws ResourceNotFoundException {
        User loggedUser = createUser();
        long userId = 2L;
        UserDto userDto = createUserDtoWithoutId();
        User updatedUser = createUser();
        UserDto updatedUserDto = createUserDto();
        when(roleService.findByAuthority(USER_ROLE)).thenReturn(createUserRole());
        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUser);
        when(userMapper.toUserDto(updatedUser)).thenReturn(updatedUserDto);

        UserDto result = userFacade.updateUser(loggedUser, userId, userDto);

        assertNotNull(result);
        assertEquals(updatedUserDto.getId(), result.getId());
        assertEquals(updatedUserDto.getName(), result.getName());
        assertEquals(updatedUserDto.getUsername(), result.getUsername());
        assertEquals(updatedUserDto.getPassword(), result.getPassword());
        assertEquals(updatedUserDto.isAdmin(), result.isAdmin());
        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
        verify(userMapper, times(1)).toUserDto(updatedUser);
    }

    @Test
    void updateUserAsAdminSuccessfully() throws ResourceNotFoundException {
        User loggedUser = createAdmin();
        long userId = 2L;
        UserDto userDto = createUserDtoWithoutId();
        User updatedUser = createUser();
        UserDto updatedUserDto = createUserDto();
        when(roleService.findByAuthority(USER_ROLE)).thenReturn(createUserRole());
        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUser);
        when(userMapper.toUserDto(updatedUser)).thenReturn(updatedUserDto);

        UserDto result = userFacade.updateUser(loggedUser, userId, userDto);

        assertNotNull(result);
        assertEquals(updatedUserDto.getId(), result.getId());
        assertEquals(updatedUserDto.getName(), result.getName());
        assertEquals(updatedUserDto.getUsername(), result.getUsername());
        assertEquals(updatedUserDto.getPassword(), result.getPassword());
        assertEquals(updatedUserDto.isAdmin(), result.isAdmin());
        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
        verify(userMapper, times(1)).toUserDto(updatedUser);
    }

    @Test
    void updateUserAdminChangeRole() throws ResourceNotFoundException {
        User loggedUser = createAdmin();
        long userId = 2L;
        UserDto userDto = createAdminDtoWithoutId();
        User updatedUser = createUser();
        UserDto updatedUserDto = createAdminDto();
        when(roleService.findByAuthority(ADMIN_ROLE)).thenReturn(createAdminRole());
        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUser);
        when(userMapper.toUserDto(updatedUser)).thenReturn(updatedUserDto);

        UserDto result = userFacade.updateUser(loggedUser, userId, userDto);

        assertNotNull(result);
        assertEquals(updatedUserDto.getId(), result.getId());
        assertEquals(updatedUserDto.getName(), result.getName());
        assertEquals(updatedUserDto.getUsername(), result.getUsername());
        assertEquals(updatedUserDto.getPassword(), result.getPassword());
        assertEquals(updatedUserDto.isAdmin(), result.isAdmin());
        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
        verify(userMapper, times(1)).toUserDto(updatedUser);
    }

    @Test
    void updateUserChangeRoleThrowsException() throws ResourceNotFoundException {
        User loggedUser = createUser();
        long userId = 2L;
        UserDto userDto = createAdminDtoWithoutId();

        final String expectedMessage = "User is not allowed to update user role";
        final AccessDeniedException actualException = assertThrows(AccessDeniedException.class, () -> {
            userFacade.updateUser(loggedUser, userId, userDto);
        });

        assertEquals(expectedMessage, actualException.getMessage());
        verify(userService, never()).updateUser(anyLong(), any());
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

        final String expectedMessage = "User with id '1L' was not found";
        final ResourceNotFoundException actualException = assertThrows(ResourceNotFoundException.class, () -> {
            userFacade.deleteUser(userId);
        });

        assertEquals(expectedMessage, actualException.getMessage());
        verify(userService, times(1)).deleteUser(userId);
    }

    private static Stream<Arguments> provideUserData() {
        return Stream.of(
                Arguments.of(createAdmin(), createAdminDto()),
                Arguments.of(createUser(), createUserDto())
        );
    }

    private static Stream<Arguments> provideUserCreationData() {
        return Stream.of(
                Arguments.of(createAdminDtoWithoutId(), createAdminWithoutId(), createAdmin(), createAdminDto()),
                Arguments.of(createUserDtoWithoutId(), createUserWithoutId(), createUser(), createUserDto())
        );
    }

    private static Role createUserRole() {
        return Role.builder().id(1L).authority(USER_ROLE).build();
    }

    private static Role createAdminRole() {
        return Role.builder().id(2L).authority(ADMIN_ROLE).build();
    }

    private static User createAdmin() {
        return User.builder()
                .id(1L)
                .name("Alice")
                .username("alice")
                .password("Password123")
                .authorities(Set.of(createAdminRole()))
                .build();
    }

    private static UserDto createAdminDto() {
        return UserDto.builder()
                .id(1L)
                .name("Alice")
                .username("alice")
                .password("Password123")
                .admin(true)
                .build();
    }

    private static UserDto createAdminDtoWithoutId() {
        return UserDto.builder()
                .name("Alice")
                .username("alice")
                .password("Password123")
                .admin(true)
                .build();
    }

    private static User createAdminWithoutId() {
        return User.builder()
                .name("Alice")
                .username("alice")
                .password("Password123")
                .authorities(Set.of(createAdminRole()))
                .build();
    }

    private static User createUserWithoutId() {
        return User.builder()
                .name("Bob")
                .username("bob")
                .password("Password123")
                .authorities(Set.of(createUserRole()))
                .build();
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

    private static UserDto createUserDto() {
        return UserDto.builder()
                .id(2L)
                .name("Bob")
                .username("bob")
                .password("Password123")
                .admin(false)
                .build();
    }

    private static UserDto createUserDtoWithoutId() {
        return UserDto.builder()
                .name("Bob")
                .username("bob")
                .password("Password123")
                .admin(false)
                .build();
    }
}
