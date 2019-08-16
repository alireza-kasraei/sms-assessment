package net.devk.sms.language.dto;

public class CreateNewLocale {

	private String locale;
	private CreateOrUpdateLanguage language;

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public CreateOrUpdateLanguage getLanguage() {
		return language;
	}

	public void setLanguage(CreateOrUpdateLanguage language) {
		this.language = language;
	}

}
