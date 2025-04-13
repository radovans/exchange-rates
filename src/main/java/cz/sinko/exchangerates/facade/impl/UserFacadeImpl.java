package cz.sinko.exchangerates.facade.impl;

import cz.sinko.exchangerates.configuration.exception.AlreadyExistsException;
import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.facade.UserFacade;
import cz.sinko.exchangerates.repository.entity.Role;
import cz.sinko.exchangerates.repository.entity.User;
import cz.sinko.exchangerates.service.RoleService;
import cz.sinko.exchangerates.service.UserService;
import cz.sinko.exchangerates.service.dto.user.UserDto;
import cz.sinko.exchangerates.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cz.sinko.exchangerates.configuration.Constants.ADMIN_ROLE;
import static cz.sinko.exchangerates.configuration.Constants.USER_ROLE;

/**
 * Implementation of {@link UserFacade}
 *
 * @author Radovan Šinko
 */
@Component
@AllArgsConstructor
@Slf4j
public class UserFacadeImpl implements UserFacade {

    private final RoleService roleService;

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    public UserDto getUser(final long id) throws ResourceNotFoundException {
        log.info("Getting user with id: '{}'", id);
        final UserDto userDto = userMapper.toUserDto(userService.find(id));
        log.info("Successfully retrieved user with id '{}'", id);
        return userDto;
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("Getting all users");
        final List<User> users = userService.find(Sort.by("id").ascending());
        log.info("Successfully retrieved '{}' users", users.size());
        return userMapper.toUsers(users);
    }

    @Override
    public UserDto createUser(final UserDto userDto) throws AlreadyExistsException {
        log.info("Creating user: '{}'", userDto);

        final String roleName = Boolean.TRUE.equals(userDto.isAdmin()) ? ADMIN_ROLE : USER_ROLE;
        final Role role = roleService.findByAuthority(roleName);

        final Set<Role> authorities = new HashSet<>();
        authorities.add(role);

        final User user = User.builder()
                .name(userDto.getName())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .authorities(authorities)
                .build();

        final UserDto createdUserDto = userMapper.toUserDto(userService.createUser(user));
        log.info("User successfully created with id '{}'", createdUserDto.getId());
        return createdUserDto;
    }

    @Override
    public void deleteUser(final long id) throws ResourceNotFoundException {
        log.info("Deleting user with id '{}'", id);
        userService.deleteUser(id);
        log.info("User successfully deleted with id '{}'", id);

    }

    @Override
    public UserDto updateUser(final User loggedUser,
                              final long id,
                              final UserDto userDto) throws ResourceNotFoundException {

        log.info("Updating user with id '{}' and user '{}'", id, userDto);

        final boolean hasUserRole =
                loggedUser.getAuthorities().stream().anyMatch(r -> USER_ROLE.equals(r.getAuthority()));
        final boolean hasAdminRole =
                loggedUser.getAuthorities().stream().anyMatch(r -> ADMIN_ROLE.equals(r.getAuthority()));

        validateUpdate(loggedUser, id, userDto, hasUserRole);

        final Set<Role> authorities = new HashSet<>();
        if (hasAdminRole) {
            final String roleName = Boolean.TRUE.equals(userDto.isAdmin()) ? ADMIN_ROLE : USER_ROLE;
            final Role role = roleService.findByAuthority(roleName);
            authorities.add(role);
        }

        final User user = User.builder()
                .name(userDto.getName())
                .password(userDto.getPassword())
                .authorities(authorities)
                .build();

        final UserDto updatedUserDto = userMapper.toUserDto(userService.updateUser(id, user));
        log.info("User successfully updated with id '{}'", id);
        return updatedUserDto;
    }

    private static void validateUpdate(User loggedUser, long id, UserDto userDto, boolean hasUserRole) {
        if (hasUserRole && !loggedUser.getId().equals(id)) {
            log.warn("Access denied for user '{}' to update user with id '{}'", loggedUser.getUsername(), id);
            throw new AccessDeniedException("User is not allowed to update other users");
        }
        if (hasUserRole && userDto.isAdmin()) {
            log.warn("Access denied for user '{}' to update user role", loggedUser.getUsername());
            throw new AccessDeniedException("User is not allowed to update user role");
        }
    }
}
