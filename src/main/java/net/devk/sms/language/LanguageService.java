package net.devk.sms.language;

import java.util.List;

import net.devk.sms.language.model.Language;

/**
 * Service interface for interacting with Language Service
 */
public interface LanguageService {

	/**
	 * @return List of Registered Locales in the file system. like EN, DE, FA...
	 */
	List<String> getCurrentLocales();

	/**
	 * @param locale locale code
	 * @return List of {@link Language} based on the given locale code
	 */
	List<Language> getLanguages(String locale);

	/**
	 * adds an entry for a given locale based on id and content
	 * 
	 * @param locale  for adding language
	 * @param content of the language
	 * @param id      of the language
	 */
	void addEntry(String locale, String content, String id);

	/**
	 * edits an entry for a given locale based on id and content
	 * 
	 * @param locale  for editing language
	 * @param content of the language
	 * @param id      of the language
	 */
	void editEntry(String locale, String content, String id);

	/**
	 * deletes an entry, based on the given locale and if
	 * 
	 * @param locale for deleting language
	 * @param id     of the language
	 */
	void deleteEntry(String locale, String id);

	/**
	 * creates an Locale file with the given language data
	 * 
	 * @param locale  name of the locale , EN, DE, FA
	 * @param content of the language
	 * @param id      of the language
	 */
	void createLocale(String locale, String content, String id);

	/**
	 * @param locale
	 * @return list of {@link Language} which are not translated and by convention
	 *         has the same value for their ids
	 */
	List<Language> getUntranslatedLanguages(String locale);

	/**
	 * @param locale
	 * @return list of duplicate ids
	 */
	List<String> getConflicts(String locale);

	/**
	 * completely remove language entries with duplicate ids
	 * 
	 */
	void removeDuplicates(String locale);

}
