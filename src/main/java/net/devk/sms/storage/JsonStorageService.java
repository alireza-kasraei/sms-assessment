package net.devk.sms.storage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.devk.sms.language.model.LanguageFile;

public class JsonStorageService implements StorageService {

	private static final String FILE_EXTENSION = ".json";
	private static final String DEFAULT_FILENAME = "language_";

	private ObjectMapper objectMapper;
	private File rootDirectory;

	public JsonStorageService(ObjectMapper objectMapper, String rootPath) {
		this.objectMapper = objectMapper;
		rootDirectory = StorageService.getRootDirectory(rootPath);
	}

	@Override
	public void persist(LanguageFile languageXmlFile) {
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(rootDirectory.getAbsolutePath() + File.separator + DEFAULT_FILENAME
					+ languageXmlFile.getLocale() + FILE_EXTENSION), languageXmlFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LanguageFile read(String locale) {
		try {
			return objectMapper.readValue(new File(
					rootDirectory.getAbsolutePath() + File.separator + DEFAULT_FILENAME + locale + FILE_EXTENSION),
					LanguageFile.class);
		} catch (IOException e) {
			throw new InvalidLocaleFileException(e);
		}
	}

	@Override
	public List<String> getCurrentLocales() {
		return Stream.of(rootDirectory.listFiles()).filter(File::isFile)
				.filter(f -> f.getName().startsWith(DEFAULT_FILENAME))
				.map(f -> f.getName().substring(DEFAULT_FILENAME.length(), f.getName().length() - 5))
				.collect(Collectors.toList());
	}

}
