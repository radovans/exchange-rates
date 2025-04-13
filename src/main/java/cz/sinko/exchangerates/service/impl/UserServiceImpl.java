package cz.sinko.exchangerates.service.impl;

import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.repository.UserRepository;
import cz.sinko.exchangerates.repository.entity.User;
import cz.sinko.exchangerates.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public User find(final long id) throws ResourceNotFoundException {
        log.info("Finding user with id: '{}'", id);
        return userRepository.findById(id).orElseThrow(() ->
                ResourceNotFoundException.createWith("User", "with id '" + id + "' was not found"));
    }

    @Override
    public List<User> find(final Sort sort) {
        log.info("Finding all users with sort: '{}'", sort);
        return userRepository.findAll(sort);
    }

    @Override
    @Transactional
    public User createUser(final User user) {
        log.info("Creating user: '{}'", user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(final long id) throws ResourceNotFoundException {
        log.info("Deleting user with id: '{}'", id);
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException.createWith("User", "with id '" + id + "' was not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User updateUser(final long id, final User user) throws ResourceNotFoundException {
        log.info("Updating user with id: '{}', '{}'", id, user);
        final User existingUser = find(id);
        existingUser.setName(user.getName());
        return userRepository.save(existingUser);
    }
}
