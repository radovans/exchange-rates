package cz.sinko.exchangerates.configuration.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UniqueUsername annotation for validating that a username is unique in the database.
 *
 * @author Radovan Šinko
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)  // Link to the validator
public @interface UniqueUsername {

    /**
     * The message to be returned if the validation fails.
     *
     * @return the error message
     */
    String message() default "Username is already taken";

    /**
     * The groups the constraint belongs to.
     *
     * @return the groups
     */
    Class<?>[] groups() default {};

    /**
     * The payload for the annotation.
     *
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};
}
