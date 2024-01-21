package com.main.writeRoom.validation.validator;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.validation.annotation.PageLessNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageLessValidator implements ConstraintValidator<PageLessNull, Integer> {
    @Override
    public void initialize(PageLessNull constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.PAGE_LESS_NULL.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
