package net.devk.sms.language;

import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import net.devk.sms.language.model.Language;
import net.devk.sms.language.model.LanguageFile;
import net.devk.sms.storage.InvalidLocaleFileException;
import net.devk.sms.storage.StorageService;

@RunWith(MockitoJUnitRunner.class)
public class LanguageServiceTest {

	private DefaultLanguageService languageService;

	@Mock
	private StorageService storageService;

	private static LanguageFile createEnLanguage() {
		LanguageFile languageXmlFile = new LanguageFile();
		languageXmlFile.setLocale("EN");
		Language language = new Language();
		language.setContent("hello");
		language.setId("@hello@");
		languageXmlFile.getLanguage().add(language);
		return languageXmlFile;
	}

	private static LanguageFile createDuplicateEnLanguage() {
		LanguageFile languageXmlFile = new LanguageFile();
		languageXmlFile.setLocale("EN");
		Language language = new Language();
		language.setContent("hello");
		language.setId("@hello@");
		Language language2 = new Language();
		language2.setContent("hello2");
		language2.setId("@hello@");
		languageXmlFile.getLanguage().add(language);
		languageXmlFile.getLanguage().add(language2);
		return languageXmlFile;
	}

	private static LanguageFile createEnLanguageWithoutContent() {
		LanguageFile languageXmlFile = new LanguageFile();
		languageXmlFile.setLocale("EN");
		Language language = new Language();
		language.setContent("@hello@");
		language.setId("@hello@");
		languageXmlFile.getLanguage().add(language);
		return languageXmlFile;
	}

	@Before
	public void init() throws IOException {
		languageService = new DefaultLanguageService(storageService);
	}

	@Test
	public void testGetCurrentLanguages() {
		given(storageService.getCurrentLocales()).willReturn(Arrays.asList("EN", "DE"));
		List<String> currentLanguages = languageService.getCurrentLocales();
		Assert.assertEquals(2, currentLanguages.size());
	}

	@Test
	public void testGetEnLocale() {
		given(storageService.read(Mockito.anyString())).willReturn(createEnLanguage());
		List<Language> languages = languageService.getLanguages("EN");
		Assert.assertEquals(1, languages.size());
	}

	@Test(expected = InvalidLocaleFileException.class)
	public void testGetInvalidLocale() {
		given(storageService.read(Mockito.anyString())).willThrow(InvalidLocaleFileException.class);
		languageService.getLanguages("EN");
	}

	@Test
	public void testCreatNewList() {
		given(storageService.read(Mockito.anyString())).willReturn(createEnLanguage());
		List<Language> languages = languageService.createNewList("EN", "content", "id");
		Assert.assertEquals(2, languages.size());
	}

	@Test
	public void testCreatNewList2() {
		given(storageService.read(Mockito.anyString())).willReturn(createEnLanguageWithoutContent());
		List<Language> languages = languageService.createNewList("EN", null, "@id@");
		Assert.assertEquals("@id@", languages.get(1).getContent());
	}

	@Test
	public void testEditList() {
		given(storageService.read(Mockito.anyString())).willReturn(createEnLanguage());
		List<Language> languages = languageService.editList("EN", "new content", "@hello@");
		Assert.assertEquals(1, languages.size());
		Assert.assertEquals("new content", languages.get(0).getContent());
	}

	@Test
	public void testDeleteEntry() {
		given(storageService.read(Mockito.anyString())).willReturn(createEnLanguage());
		List<Language> languages = languageService.deleteList("EN", "@hello@");
		Assert.assertEquals(0, languages.size());
	}

	@Test
	public void testFindUntranslatedEntries() {
		given(storageService.read(Mockito.anyString())).willReturn(createEnLanguageWithoutContent());
		List<Language> languages = languageService.getUntranslatedLanguages("EN");
		Assert.assertEquals(1, languages.size());
	}

	@Test
	public void testFindUntranslatedEntries2() {
		given(storageService.read(Mockito.anyString())).willReturn(createEnLanguage());
		List<Language> languages = languageService.getUntranslatedLanguages("EN");
		Assert.assertEquals(0, languages.size());
	}

	@Test
	public void testFindConflicts() {
		given(storageService.read(Mockito.anyString())).willReturn(createDuplicateEnLanguage());
		List<String> languages = languageService.getConflicts("EN");
		Assert.assertEquals(1, languages.size());
	}

	@Test
	public void testFindConflicts2() {
		given(storageService.read(Mockito.anyString())).willReturn(createEnLanguage());
		List<String> languages = languageService.getConflicts("EN");
		Assert.assertEquals(0, languages.size());
	}

	@Test(expected = LocaleAlreadyExistsException.class)
	public void testCreateLocale() {
		given(storageService.getCurrentLocales()).willReturn(Arrays.asList("EN"));
		languageService.createLocale("EN", "1", "1");
	}

}
