package cz.sinko.exchangerates.api;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ApiError {

    @NonNull
    private List<String> errors;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stackTrace;

    private LocalDateTime timestamp = LocalDateTime.now();
}
