package ru.practicum.shareit.booking.booking_getter.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = GetterStateValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface ValidState {
    String message() default
            "Unknown state: UNSUPPORTED_STATUS";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
