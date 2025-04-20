package cz.sinko.exchangerates.api.dto.request.user;

import cz.sinko.exchangerates.configuration.validator.UniqueUsername;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Request object for creating a new User.
 */
@Data
@Builder
@ToString(exclude = "password")
@Schema(description = "Request object for creating a new user")
public class UserCreateRequest {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 50, message = "Name must be at most 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must contain only letters and spaces")
    @Schema(description = "Full name of the user", example = "Alice")
    private String name;

    @NotBlank(message = "Username must not be blank")
    @Size(max = 50, message = "Username must be at most 50 characters")
    @UniqueUsername(message = "Username must be unique")
    @Schema(description = "Unique username used for login", example = "alice")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(max = 50, message = "Password must be at most 50 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and be " +
                    "at least 8 characters long"
    )
    @Schema(description = "User's password (must meet complexity requirements)", example = "Password123")
    private String password;

    @Schema(description = "Whether the user should have admin rights", example = "false")
    private boolean admin;
}
