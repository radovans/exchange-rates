package cz.sinko.exchangerates.service;

import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.repository.entity.User;

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
}
