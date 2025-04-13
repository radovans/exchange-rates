package cz.sinko.exchangerates.service.dto.user;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for User.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
public class UserDto {

    private Long id;

    private String name;

    private String username;

    private String password;

    private boolean admin;
}
