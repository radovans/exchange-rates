package cz.sinko.exchangerates.facade;

import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.service.dto.UserDto;

/**
 * Facade for User operations.
 *
 * @author Radovan Šinko
 */
public interface UserFacade {

    /**
     * Get User by id.
     *
     * @param id User id
     * @return User DTO
     */
    UserDto getUser(long id) throws ResourceNotFoundException;
}
