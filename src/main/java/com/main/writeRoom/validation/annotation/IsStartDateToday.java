package com.main.writeRoom.validation.annotation;

import com.main.writeRoom.validation.validator.ChallengeStartDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ChallengeStartDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsStartDateToday {

    String message() default "시작날짜가 오늘부터여야 합니다.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
