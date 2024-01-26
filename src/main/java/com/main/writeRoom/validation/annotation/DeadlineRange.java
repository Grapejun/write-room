package com.main.writeRoom.validation.annotation;

import com.main.writeRoom.validation.validator.ChallengeDeadlineValidator;
import com.main.writeRoom.validation.validator.ChallengeStartDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ChallengeDeadlineValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DeadlineRange {
    String message() default "마감날짜가 일주일 단위이고, 최대 한달이어야 합니다.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
