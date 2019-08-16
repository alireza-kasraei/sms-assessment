package net.devk.sms.language;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.devk.sms.language.model.Language;
import net.devk.sms.language.model.LanguageFile;
import net.devk.sms.storage.StorageService;

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
		LanguageFile languageXmlFile = storageService.read(locale);
		return languageXmlFile.getLanguage();
	}

	public List<Language> createNewList(String locale, String content, String id) {
		Language language = new Language();
		if (content == null || content.isEmpty()) {
			language.setContent(id);
		} else {
			language.setContent(content);
		}
		language.setId(id);
		// finding languages
		List<Language> languageList = getLanguages(locale);
		languageList.add(language);
		return languageList;
	}

	public List<Language> editList(String locale, String content, String id) {
		// finding languages
		List<Language> languageList = getLanguages(locale);
		return languageList.stream().map(l -> {
			if (l.getId().equals(id)) {
				Language language = new Language();
				language.setContent(content);
				language.setId(id);
				return language;
			} else
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
		LanguageFile languageXmlFile = new LanguageFile();
		languageXmlFile.setLocale(locale);
		languageXmlFile.getLanguage().addAll(languages);
		// persist pojo
		storageService.persist(languageXmlFile);
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
		Optional<String> optional = currentLocales.stream().filter(s -> s.equals(locale)).findAny();
		if (optional.isPresent()) {
			throw new LocaleAlreadyExistsException();
		}
		Language language = new Language();
		language.setContent(content);
		language.setId(id);
		List<Language> languages = new ArrayList<>();
		languages.add(language);
		persist(locale, languages);
	}

	@Override
	public List<Language> getUntranslatedLanguages(String locale) {
		List<Language> languages = getLanguages(locale);
		return languages.stream().filter(l -> l.getContent().startsWith("@")).collect(Collectors.toList());
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

}
