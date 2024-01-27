package com.main.writeRoom.validation.validator;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.validation.annotation.DeadlineRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ChallengeDeadlineValidator implements ConstraintValidator<DeadlineRange, LocalDate> {

    @Override
    public void initialize(DeadlineRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        for (int i = 0; i < 4; i++) {
            if (value.isEqual(LocalDate.now().plusWeeks(i + 1).minusDays(1))) {
                return true;
            } else {
                continue;
            }
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(ErrorStatus.STARTDATE_NOT_TODAY.toString()).addConstraintViolation();
        return false;
    }
}
