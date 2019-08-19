package net.devk.sms.language.dto;

import lombok.Data;
import net.devk.sms.language.dto.validator.Locale;

@Data
public class CreateNewLocale {

	@Locale
	private String locale;
	private CreateOrUpdateLanguage language;

}
