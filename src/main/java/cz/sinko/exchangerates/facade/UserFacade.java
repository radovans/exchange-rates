package cz.sinko.exchangerates.facade;

import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.service.dto.UserDto;

import java.util.List;

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

    /**
     * Get all Users.
     *
     * @return list of User DTOs
     */
    List<UserDto> getUsers();

    /**
     * Create User.
     *
     * @param userDto User DTO
     * @return created User DTO
     */
    UserDto createUser(UserDto userDto);

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
     * @param id         User id
     * @param userDto    User DTO
     * @return updated User DTO
     * @throws ResourceNotFoundException if User was not found
     */
    UserDto updateUser(long id, UserDto userDto) throws ResourceNotFoundException;
}
