package net.devk.sms.storage;

import java.io.File;
import java.util.List;

import net.devk.sms.language.model.LanguageFile;

public interface StorageService {

	public void persist(LanguageFile languageFile);

	public LanguageFile read(String locale);

	public List<String> getCurrentLocales();

	public static File getRootDirectory(String path) {
		File directory = new File(path);
		if (directory.isFile()) {
			throw new RuntimeException("invalid path");
		}
		if (!directory.exists()) {
			directory.mkdirs();
		}
		return directory;
	}

}
