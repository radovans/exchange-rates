package cz.sinko.exchangerates.service.mapper;

import cz.sinko.exchangerates.repository.entity.User;
import cz.sinko.exchangerates.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper between User and UserDto.
 *
 * @author Radovan Šinko
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Get instance of UserMapper.
     *
     * @return instance of UserMapper
     */
    static UserMapper t() {
        return Mappers.getMapper(UserMapper.class);
    }

    /**
     * Map User to UserDto.
     *
     * @param source user
     * @return userDto
     */
    UserDto toUserDto(User source);
}
