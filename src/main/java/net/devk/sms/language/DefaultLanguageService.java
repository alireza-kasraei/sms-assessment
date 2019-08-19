package net.devk.sms.language;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.devk.sms.language.model.Language;
import net.devk.sms.language.model.LanguageFile;
import net.devk.sms.storage.StorageService;

/**
 * Default implementation of the {@link LanguageService}
 */
public class DefaultLanguageService implements LanguageService {

	private StorageService storageService;

	public DefaultLanguageService(StorageService storageService) {
		this.storageService = storageService;
	}

	@Override
	public List<String> getCurrentLocales() {
		return storageService.getCurrentLocales();
	}

	@Override
	public List<Language> getLanguages(String locale) {
		LanguageFile languageFile = storageService.read(locale);
		return languageFile.getLanguage();
	}

	/**
	 * Helper method for adding an entry to a list of languages
	 * 
	 * @param locale
	 * @param content
	 * @param id
	 * @return
	 */
	public List<Language> createNewList(String locale, String content, String id) {

		// finding languages
		List<Language> languageList = getLanguages(locale);
		Language language = createLanguage(id, content);
		languageList.add(language);
		return languageList;
	}

	public List<Language> editList(String locale, String content, String id) {
		// finding languages
		List<Language> languageList = getLanguages(locale);
		return languageList.stream().map(l -> {
			if (l.getId().equals(id)) {
				return createLanguage(id, content);
			}
			return l;

		}).collect(Collectors.toList());
	}

	public List<Language> deleteList(String locale, String id) {
		// finding languages
		List<Language> languageList = getLanguages(locale);
		Iterator<Language> iterator = languageList.iterator();
		while (iterator.hasNext()) {
			Language next = iterator.next();
			if (next.getId().equals(id)) {
				iterator.remove();
			}
		}
		return languageList;
	}

	private void persist(String locale, List<Language> languages) {
		LanguageFile languageFile = createLanguageFile(locale, languages);
		// persist pojo
		storageService.persist(languageFile);
	}

	@Override
	public void addEntry(String locale, String content, String id) {
		// creating entry
		List<Language> languages = createNewList(locale, content, id);
		persist(locale, languages);
	}

	@Override
	public void editEntry(String locale, String content, String id) {
		List<Language> editList = editList(locale, content, id);
		persist(locale, editList);
	}

	@Override
	public void deleteEntry(String locale, String id) {
		List<Language> deleteList = deleteList(locale, id);
		persist(locale, deleteList);
	}

	@Override
	public void createLocale(String locale, String content, String id) {
		List<String> currentLocales = getCurrentLocales();
		if (currentLocales.contains(locale)) {
			throw new LocaleAlreadyExistsException();
		}
		persist(locale, Arrays.asList(createLanguage(id, content)));
	}

	@Override
	public List<Language> getUntranslatedLanguages(String locale) {
		List<Language> languages = getLanguages(locale);
		return languages.stream().filter(DefaultLanguageService::hasIdFormat).collect(Collectors.toList());
	}

	@Override
	public List<String> getConflicts(String locale) {
		List<Language> languages = getLanguages(locale);
		return languages.stream().map(Language::getId).collect(Collectors.groupingBy(c -> c, Collectors.counting()))
				.entrySet().stream().filter(p -> p.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	@Override
	public void removeDuplicates(String locale) {
		List<String> ids = getConflicts(locale);
		ids.forEach(id -> deleteEntry(locale, id));
	}

	public static Language createLanguage(String id, String content) {
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("invalid id");
		Language language = new Language();
		if (content == null || content.isEmpty()) {
			language.setContent(id);
		} else {
			language.setContent(content);
		}
		language.setId(id);
		language.setLastModifiedDate(new Date());
		return language;
	}

	private static LanguageFile createLanguageFile(String locale, List<Language> languages) {
		LanguageFile languageFile = new LanguageFile();
		languageFile.setLocale(locale);
		languageFile.getLanguage().addAll(languages);
		return languageFile;
	}

	public static boolean hasIdFormat(Language language) {
		String content = language.getContent();
		return (content.startsWith("@") && (content.charAt(content.length() - 1) == '@'));
	}

}
