package com.main.writeRoom.validation.validator;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.validation.annotation.IsStartDateToday;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ChallengeStartDateValidator implements ConstraintValidator<IsStartDateToday, LocalDate> {
    @Override
    public void initialize(IsStartDateToday constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value.isBefore(LocalDate.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.STARTDATE_NOT_TODAY.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
