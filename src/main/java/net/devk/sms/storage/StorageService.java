package net.devk.sms.storage;

import java.util.List;

import net.devk.sms.language.model.LanguageFile;

public interface StorageService {

	public void persist(LanguageFile languageXmlFile);

	public LanguageFile read(String locale);

	public List<String> getCurrentLocales();

}
