package cz.sinko.exchangerates.api.dto.request.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * Request object for updating existing User.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
public class UserUpdateRequest {

    @Size(max = 50, message = "Name must be at most 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must contain only letters and spaces")
    private String name;

    @Size(max = 50, message = "Password must be at most 50 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and be " +
                    "at least 8 characters long")
    private String password;

    private boolean admin;
}
