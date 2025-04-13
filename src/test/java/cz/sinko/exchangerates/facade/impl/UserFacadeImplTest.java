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

    private static Stream<Arguments> provideUserData() {
        return Stream.of(
                Arguments.of(createUserAlice(), createUserDtoAlice()),
                Arguments.of(createUserBob(), createUserDtoBob())
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
