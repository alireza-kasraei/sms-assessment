package net.devk.sms.language.dto.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocaleValidator.class)
@Documented
public @interface Locale {
	String message() default "must be a valid locale." + "Found: ${validatedValue}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}