package net.devk.sms.storage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import net.devk.sms.language.model.LanguageFile;

public class XmlStorageServiceReadTest {

	private static StorageService storageService;
	private static File rootDirectory = null;

	@BeforeClass
	public static void init() throws IOException, URISyntaxException {
		ClassLoader classLoader = XmlStorageServiceReadTest.class.getClassLoader();
		//finding test-classes directory from class loader
		URL resource = classLoader.getResource(".");
		rootDirectory = new File(resource.toURI());
		storageService = new XmlStorageService(rootDirectory.getAbsolutePath());
	}

	@Test(expected = InvalidLocaleFileException.class)
	public void testReadInvalidLanguageXmlFile() throws JAXBException, IOException {
		storageService.read("fa");
	}

	@Test
	public void testReadvalidContent() throws JAXBException, IOException {

		LanguageFile languageXmlFile = storageService.read("EN");
		Assert.assertEquals("EN", languageXmlFile.getLocale());
		Assert.assertEquals(1, languageXmlFile.getLanguage().size());
		Assert.assertEquals("None", languageXmlFile.getLanguage().get(0).getContent());
		Assert.assertEquals("@id/area/none@", languageXmlFile.getLanguage().get(0).getId());
	}

	@Test
	public void testReadBigLanguageFile() throws JAXBException, IOException {
		LanguageFile languageXmlFile = storageService.read("DE");
		Assert.assertEquals(2745, languageXmlFile.getLanguage().size());
	}

	@Test
	public void testGetCurrentLanguages() {
		List<String> currentLocales = storageService.getCurrentLocales();
		Assert.assertEquals(2, currentLocales.size());
	}

}
