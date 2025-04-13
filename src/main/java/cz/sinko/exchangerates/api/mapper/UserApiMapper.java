package cz.sinko.exchangerates.api.mapper;

import cz.sinko.exchangerates.api.dto.response.user.UserResponse;
import cz.sinko.exchangerates.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * Mapper between User and Request/Response UserDto.
 *
 * @author Radovan Šinko
 */
@Mapper(componentModel = "spring")
public interface UserApiMapper {

    /**
     * Get instance of UserMapper.
     *
     * @return instance of UserMapper
     */
    static UserApiMapper t() {
        return Mappers.getMapper(UserApiMapper.class);
    }

    /**
     * Map UserDto to UserResponse.
     *
     * @param source userDto
     * @return userResponse
     */
    UserResponse toResponse(UserDto source);
}
