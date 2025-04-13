package cz.sinko.exchangerates.configuration.validator;

import cz.sinko.exchangerates.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Class containing constants used in the application.
 *
 * @author Radovan Šinko
 */
@Component
@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository userRepository;

    /**
     * Validates if the username is unique.
     *
     * @param username the username to validate
     * @param context  the validation context
     * @return true if the username is unique, false otherwise
     */
    @Override
    public boolean isValid(final String username, final ConstraintValidatorContext context) {
        return username == null || userRepository.findByUsername(username).isEmpty();
    }

    /**
     * Initializes the validator.
     *
     * @param constraintAnnotation the constraint annotation
     */
    @Override
    public void initialize(final UniqueUsername constraintAnnotation) {
        // No initialization needed
    }
}
