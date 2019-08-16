package net.devk.sms.language;

import java.util.List;

import net.devk.sms.language.model.Language;

public interface LanguageService {

	List<String> getCurrentLocales();

	List<Language> getLanguages(String string);

	void addEntry(String locale, String content, String id);

	void editEntry(String locale, String content, String id);

	void deleteEntry(String locale, String id);

	void createLocale(String locale, String content, String id);

	List<Language> getUntranslatedLanguages(String locale);

	List<String> getConflicts(String locale);

	void removeDuplicates(String locale);

}
