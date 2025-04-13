package cz.sinko.exchangerates.api.dto.response.user;

import lombok.Builder;
import lombok.Data;

/**
 * Response object for User.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String name;

    private boolean admin;
}
