package cz.sinko.exchangerates.service.mapper;

import cz.sinko.exchangerates.repository.entity.User;
import cz.sinko.exchangerates.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

    /**
     * Map UserDto to User
     *
     * @param source userDto
     * @return user
     */
    User toUser(UserDto source);

    /**
     * Map list of Users to list of UserDtos.
     *
     * @param users list of users
     * @return list of userDtos
     */
    List<UserDto> toUsers(List<User> users);

    /**
     * Map list of UserDtos to list of Users.
     *
     * @param userDtos list of userDtos
     * @return list of users
     */
    List<User> toUserDtos(List<UserDto> userDtos);
}
