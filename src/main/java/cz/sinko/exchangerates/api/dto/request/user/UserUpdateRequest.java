package cz.sinko.exchangerates.api.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Request object for updating existing User.
 */
@Data
@Builder
@ToString(exclude = "password")
@Schema(description = "Request object for updating an existing user")
public class UserUpdateRequest {

    @Size(max = 50, message = "Name must be at most 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must contain only letters and spaces")
    @Schema(description = "Full name of the user", example = "Alice")
    private String name;

    @Size(max = 50, message = "Password must be at most 50 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and be " +
                    "at least 8 characters long"
    )
    @Schema(description = "New password for the user (if updating)", example = "NewPass123")
    private String password;

    @Schema(description = "Whether the user should have admin rights", example = "true")
    private boolean admin;
}
