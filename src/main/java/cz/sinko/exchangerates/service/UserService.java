package cz.sinko.exchangerates.service;

import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.repository.entity.User;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Service for User operations.
 *
 * @author Radovan Šinko
 */
public interface UserService {

    /**
     * Find User by id.
     *
     * @param id User id
     * @return User
     * @throws ResourceNotFoundException if User was not found
     */
    User find(long id) throws ResourceNotFoundException;

    /**
     * Find all Users.
     *
     * @param id Sort
     * @return list of Users
     */
    List<User> find(Sort id);

    /**
     * Create User.
     *
     * @param user User
     * @return created User
     */
    User createUser(User user);

    /**
     * Delete User by id.
     *
     * @param id User id
     * @throws ResourceNotFoundException if User was not found
     */
    void deleteUser(long id) throws ResourceNotFoundException;

    /**
     * Update User by id.
     *
     * @param id   User id
     * @param user User
     * @return updated User
     * @throws ResourceNotFoundException if User was not found
     */
    User updateUser(long id, User user) throws ResourceNotFoundException;
}
