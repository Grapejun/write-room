package com.main.writeRoom.validation.annotation;

import com.main.writeRoom.validation.validator.PageLessValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PageLessValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PageLessNull {
    String message() default "page는 0부터 입니다.";
    Class<?> [] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
