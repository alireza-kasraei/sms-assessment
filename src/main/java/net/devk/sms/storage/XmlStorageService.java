package net.devk.sms.storage;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.devk.sms.language.model.LanguageFile;

public class XmlStorageService implements StorageService {

	private static final String FILE_EXTENSION = ".xml";
	private static final String DEFAULT_FILENAME = "language_";
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	private File rootDirectory;

	public XmlStorageService(String path) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(LanguageFile.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
			unmarshaller = jaxbContext.createUnmarshaller();
			this.rootDirectory = getRootDirectory(path);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized void persist(LanguageFile languageXmlFile) {
		try {
			marshaller.marshal(languageXmlFile, new File(rootDirectory.getAbsolutePath() + File.separator
					+ DEFAULT_FILENAME + languageXmlFile.getLocale() + FILE_EXTENSION));
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized LanguageFile read(String locale) {
		try {
			LanguageFile languageXmlFile = (LanguageFile) unmarshaller.unmarshal(new File(
					rootDirectory.getAbsolutePath() + File.separator + DEFAULT_FILENAME + locale + FILE_EXTENSION));
			languageXmlFile.setLocale(locale);
			return languageXmlFile;
		} catch (JAXBException e) {
			throw new InvalidLocaleFileException(e);
		}
	}

	@Override
	public List<String> getCurrentLocales() {

		return Stream.of(rootDirectory.listFiles()).filter(File::isFile)
				.filter(f -> f.getName().startsWith(DEFAULT_FILENAME))
				.map(f -> f.getName().substring(DEFAULT_FILENAME.length(), f.getName().length() - 4))
				.collect(Collectors.toList());
	}

	private static File getRootDirectory(String path) {
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
