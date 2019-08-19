package net.devk.sms.language.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocaleValidator implements ConstraintValidator<Locale, String> {

	@Override
	public void initialize(Locale constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}

		if (value.length() != 2)
			return false;

		return value.chars().anyMatch(ch -> (ch < 97) || (ch > 122));
	}
}