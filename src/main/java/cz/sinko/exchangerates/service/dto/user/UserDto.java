package cz.sinko.exchangerates.service.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * DTO for User.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
@ToString(exclude = "password")
public class UserDto {

    private Long id;

    private String name;

    private String username;

    private String password;

    private boolean admin;
}
