package cz.sinko.exchangerates.api.dto.response.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * Response object for User.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
@Schema(description = "User response object")
public class UserResponse {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Username used to log in", example = "alice")
    private String username;

    @Schema(description = "Full name of the user", example = "Alice")
    private String name;

    @Schema(description = "Is the user admin", example = "false")
    private boolean admin;
}
