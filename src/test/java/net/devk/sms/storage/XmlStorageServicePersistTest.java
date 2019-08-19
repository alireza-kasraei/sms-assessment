package net.devk.sms.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import net.devk.sms.language.model.Language;
import net.devk.sms.language.model.LanguageFile;

public class XmlStorageServicePersistTest {

	private static StorageService storageService;

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();
	private File rootDirectory = null;

	@Before
	public void init() throws IOException {
		// creates temp directory
		rootDirectory = temporaryFolder.newFolder("languages");
		// create storage service for testing persistence
		storageService = new XmlStorageService(rootDirectory.getAbsolutePath());
	}

	@Test
	public void testPersisLanguageXmlFile() throws JAXBException, IOException {

		// arrange
		File output = rootDirectory.toPath().resolve("language_EN.xml").toFile();

		LanguageFile languageXmlFile = new LanguageFile();
		Language language = new Language();
		language.setContent("None");
		language.setId("@id/area/none@");
		languageXmlFile.setLocale("EN");
		languageXmlFile.getLanguage().add(language);
		storageService.persist(languageXmlFile);
		// assert
		assertThat(output).hasName("language_EN.xml");
		ClassLoader classLoader = getClass().getClassLoader();
		Diff diff = DiffBuilder.compare(Input.fromFile(output.getAbsolutePath()))
				.withTest(Input.fromStream(classLoader.getResource("language_EN.xml").openStream()))
				.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)).ignoreWhitespace().ignoreComments()
				.checkForSimilar().build();

		Assert.assertTrue(!diff.hasDifferences());
	}

}
