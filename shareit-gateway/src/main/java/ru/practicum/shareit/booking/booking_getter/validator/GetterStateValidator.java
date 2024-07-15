package ru.practicum.shareit.booking.booking_getter.validator;

import org.springframework.context.annotation.Configuration;
import ru.practicum.shareit.booking.booking_getter.model.BookingGetterState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@Configuration
public class GetterStateValidator implements ConstraintValidator<ValidState, String> {

    @Override
    public boolean isValid(final String state, final ConstraintValidatorContext context) {
        return Arrays.toString(BookingGetterState.values()).contains(state);
    }
}
