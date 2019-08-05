package com.zetcode;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocaleLangValidator.class)
@Documented
public @interface LocaleLang {
    String message () default "must be a valid language. Found: ${validatedValue}";

    Class<?>[] groups () default {};

    Class<? extends Payload>[] payload () default {};
}
