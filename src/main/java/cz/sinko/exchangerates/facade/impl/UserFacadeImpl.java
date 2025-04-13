package cz.sinko.exchangerates.facade.impl;

import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.facade.UserFacade;
import cz.sinko.exchangerates.repository.entity.User;
import cz.sinko.exchangerates.service.UserService;
import cz.sinko.exchangerates.service.dto.UserDto;
import cz.sinko.exchangerates.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of {@link UserFacade}
 *
 * @author Radovan Šinko
 */
@Component
@AllArgsConstructor
@Slf4j
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    public UserDto getUser(final long id) throws ResourceNotFoundException {
        log.info("Getting user with id: '{}'", id);
        return userMapper.toUserDto(userService.find(id));
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("Getting all users");
        final List<User> users = userService.find(Sort.by("id").ascending());
        return userMapper.toUsers(users);
    }

    @Override
    public UserDto createUser(final UserDto userDto) {
        log.info("Creating user: '{}'", userDto);

        final User user = User.builder()
                .name(userDto.getName())
                .build();

        return userMapper.toUserDto(userService.createUser(user));
    }

    @Override
    public void deleteUser(final long id) throws ResourceNotFoundException {
        log.info("Deleting user with id: '{}'", id);
        userService.deleteUser(id);
    }

    @Override
    public UserDto updateUser(final long id, final UserDto userDto) throws ResourceNotFoundException {
        log.info("Updating user with id: '{}', '{}'", id, userDto);

        final User user = User.builder()
                .name(userDto.getName())
                .build();

        return userMapper.toUserDto(userService.updateUser(id, user));
    }
}
