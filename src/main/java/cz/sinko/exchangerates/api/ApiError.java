package cz.sinko.exchangerates.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class representing an API error response.
 *
 * @author Radovan Šinko
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(description = "API error response")
public class ApiError {

    @Schema(description = "List of error messages")
    @NonNull
    private List<String> errors;

    @Schema(description = "Time of the error", example = "2025-04-20T08:45:47.37513")
    private LocalDateTime timestamp = LocalDateTime.now();
}
