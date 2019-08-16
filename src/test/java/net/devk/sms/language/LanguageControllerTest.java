package net.devk.sms.language;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import net.devk.sms.language.model.Language;
import net.devk.sms.storage.InvalidLocaleFileException;

@RunWith(SpringRunner.class)
@WebMvcTest(LanguageController.class)
public class LanguageControllerTest {

	@MockBean
	private LanguageService languageService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetCurrentLocales() throws Exception {
		given(languageService.getCurrentLocales()).willReturn(Arrays.asList("EN", "DE"));
		mockMvc.perform(get("/locales").accept(MediaType.APPLICATION_JSON)).andExpect(content().json("[\"EN\",\"DE\"]"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetValidLanguages() throws Exception {
		Language language = new Language();
		language.setContent("hello");
		language.setId("@hello@");
		given(languageService.getLanguages(Mockito.anyString())).willReturn(Arrays.asList(language));
		mockMvc.perform(get("/languages?locale=EN").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[{\"content\":\"hello\",\"id\":\"@hello@\"}]")).andExpect(status().isOk());
	}

	@Test
	public void testGetLanguagesWithDefaultLocale() throws Exception {
		Language language = new Language();
		language.setContent("hello");
		language.setId("@hello@");
		given(languageService.getLanguages(Mockito.anyString())).willReturn(Arrays.asList(language));
		mockMvc.perform(get("/languages").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[{\"content\":\"hello\",\"id\":\"@hello@\"}]")).andExpect(status().isOk());
	}

	@Test
	public void testGetInValidLanguages() throws Exception {
		given(languageService.getLanguages(Mockito.anyString())).willThrow(InvalidLocaleFileException.class);
		mockMvc.perform(get("/languages?locale=FA").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testPostLanguages() throws Exception {
		doNothing().when(languageService).addEntry(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(post("/languages?locale=EN").contentType(MediaType.APPLICATION_JSON)
				.content("{\"content\":\"hello\",\"id\":\"@hello@\"}")).andExpect(status().isCreated());
	}

	@Test
	public void testPostLanguagesForInvalidLocale() throws Exception {
		doThrow(InvalidLocaleFileException.class).when(languageService).addEntry(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(post("/languages?locale=EN").contentType(MediaType.APPLICATION_JSON)
				.content("{\"content\":\"hello\",\"id\":\"@hello@\"}")).andExpect(status().isBadRequest());
	}

	@Test
	public void testPutLanguagesForInvalidLocale() throws Exception {
		doThrow(InvalidLocaleFileException.class).when(languageService).editEntry(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(put("/languages?locale=EN").contentType(MediaType.APPLICATION_JSON)
				.content("{\"content\":\"hello\",\"id\":\"@hello@\"}")).andExpect(status().isBadRequest());
	}

	@Test
	public void testPutLanguagesForValidLocale() throws Exception {
		doNothing().when(languageService).editEntry(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(put("/languages?locale=EN").contentType(MediaType.APPLICATION_JSON)
				.content("{\"content\":\"hello\",\"id\":\"@hello@\"}")).andExpect(status().isNoContent());
	}

	@Test
	public void testDeleteLanguagesForValidLocale() throws Exception {
		doNothing().when(languageService).deleteEntry(Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(delete("/languages/@hello@?locale=EN").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testDeleteLanguagesForInValidLocale() throws Exception {
		doThrow(InvalidLocaleFileException.class).when(languageService).deleteEntry(Mockito.anyString(),
				Mockito.anyString());
		mockMvc.perform(delete("/languages/@hello@?locale=FA").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateNewLocale() throws Exception {
		doNothing().when(languageService).createLocale(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(post("/locales").contentType(MediaType.APPLICATION_JSON)
				.content("{\"locale\":\"FA\",\"language\":{\"content\":\"hello\",\"id\":\"@hello@\"}}"))
				.andExpect(status().isCreated());
	}

	@Test
	public void testGetUntranslated() throws Exception {
		Language language = new Language();
		language.setContent("hello");
		language.setId("@hello@");
		given(languageService.getUntranslatedLanguages(Mockito.anyString())).willReturn(Arrays.asList(language));
		mockMvc.perform(get("/untranslated")).andExpect(status().isOk())
				.andExpect(content().json("[{\"content\":\"hello\",\"id\":\"@hello@\"}]"));
	}

	@Test
	public void testGetConflicts() throws Exception {
		given(languageService.getConflicts(Mockito.anyString())).willReturn(Arrays.asList("@hello@"));
		mockMvc.perform(get("/conflicts")).andExpect(status().isOk()).andExpect(content().json("[\"@hello@\"]"));
	}

}
