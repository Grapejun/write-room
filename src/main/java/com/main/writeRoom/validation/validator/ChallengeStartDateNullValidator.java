package com.main.writeRoom.validation.validator;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.validation.annotation.IsStartDateTodayNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ChallengeStartDateNullValidator implements ConstraintValidator<IsStartDateTodayNull, LocalDate> {
    @Override
    public void initialize(IsStartDateTodayNull constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null || !(value.isBefore(LocalDate.now()))) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(ErrorStatus.STARTDATE_NOT_TODAY.toString()).addConstraintViolation();
        return false;
    }
}
