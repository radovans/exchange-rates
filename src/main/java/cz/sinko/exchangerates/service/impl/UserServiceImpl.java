package cz.sinko.exchangerates.service.impl;

import cz.sinko.exchangerates.configuration.exception.AlreadyExistsException;
import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.repository.UserRepository;
import cz.sinko.exchangerates.repository.entity.User;
import cz.sinko.exchangerates.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserService}
 *
 * @author Radovan Šinko
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User find(final long id) throws ResourceNotFoundException {
        return findUserByIdOrThrow(id);
    }

    @Override
    public List<User> find(final Sort sort) {
        log.info("Finding all users with sort '{}'", sort);
        final List<User> users = userRepository.findAll(sort);
        log.info("Found '{}' users", users.size());
        return users;
    }

    @Override
    @Transactional
    public User createUser(final User user) throws AlreadyExistsException {
        log.info("Creating user '{}'", user);
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw AlreadyExistsException.createWith("User", "with username '" + user.getUsername() + "' was found");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        final User createdUser = userRepository.save(user);
        log.info("User created with id '{}'", createdUser.getId());
        return createdUser;
    }

    @Override
    @Transactional
    public void deleteUser(final long id) throws ResourceNotFoundException {
        log.info("Deleting user with id '{}'", id);
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException.createWith("User", "with id '" + id + "' was not found");
        }
        userRepository.deleteById(id);
        log.info("User deleted with id '{}'", id);
    }

    @Override
    @Transactional
    public User updateUser(final long id, final User user) throws ResourceNotFoundException {
        log.info("Updating user with id '{}', '{}'", id, user);
        final User existingUser = findUserByIdOrThrow(id);

        if (StringUtils.isNotBlank(user.getUsername()) && !existingUser.getUsername().equals(user.getUsername())) {
            log.info("Username is not allowed to be changed");
            throw new IllegalArgumentException("Username is not allowed to be changed");
        }
        if (StringUtils.isNotBlank(user.getName())) {
            existingUser.setName(user.getName());
        }
        if (StringUtils.isNotBlank(user.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (ObjectUtils.isNotEmpty(user.getAuthorities())) {
            existingUser.setAuthorities(user.getAuthorities());
        }
        final User updatedUser = userRepository.save(existingUser);
        log.info("User updated with id '{}'", updatedUser.getId());
        return updatedUser;
    }

    private User findUserByIdOrThrow(final long id) throws ResourceNotFoundException {
        log.info("Finding user with id '{}'", id);
        final Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.info("User with id '{}' was not found", id);
            throw ResourceNotFoundException.createWith("User", "with id '" + id + "' was not found");
        }
        return user.get();
    }
}
