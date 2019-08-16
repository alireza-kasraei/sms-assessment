package net.devk.sms.language;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.devk.sms.language.dto.CreateNewLocale;
import net.devk.sms.language.dto.CreateOrUpdateLanguage;
import net.devk.sms.language.model.Language;

@RestController
public class LanguageController {

	private LanguageService languageService;

	public LanguageController(LanguageService languageService) {
		this.languageService = languageService;
	}

	@GetMapping("/locales")
	public ResponseEntity<List<String>> getCurrentLocales() {
		return ResponseEntity.ok(languageService.getCurrentLocales());
	}

	@PostMapping("/locales")
	public ResponseEntity<?> createNewLocale(@RequestBody CreateNewLocale newLocale) {
		languageService.createLocale(newLocale.getLocale(), newLocale.getLanguage().getContent(),
				newLocale.getLanguage().getId());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/languages")
	public ResponseEntity<List<Language>> getLanguages(
			@RequestParam(name = "locale", defaultValue = "EN") String locale) {
		return ResponseEntity.ok(languageService.getLanguages(locale));
	}

	@PostMapping("/languages")
	public ResponseEntity<?> addLanguages(@RequestBody CreateOrUpdateLanguage newLanguage,
			@RequestParam(name = "locale", defaultValue = "EN") String locale) {
		languageService.addEntry(locale, newLanguage.getContent(), newLanguage.getId());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/languages")
	public ResponseEntity<?> editLanguages(@RequestBody CreateOrUpdateLanguage newLanguage,
			@RequestParam(name = "locale", defaultValue = "EN") String locale) {
		languageService.editEntry(locale, newLanguage.getContent(), newLanguage.getId());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/languages/{id}")
	public ResponseEntity<?> deleteLanguages(@PathVariable(name = "id", required = true) String id,
			@RequestParam(name = "locale", defaultValue = "EN") String locale) {
		languageService.deleteEntry(locale, id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/untranslated")
	public ResponseEntity<?> getUntranslatedLanguages(
			@RequestParam(name = "locale", defaultValue = "EN") String locale) {
		return ResponseEntity.status(HttpStatus.OK).body(languageService.getUntranslatedLanguages(locale));
	}

	@GetMapping("/conflicts")
	public ResponseEntity<?> getConflictIds(@RequestParam(name = "locale", defaultValue = "EN") String locale) {
		return ResponseEntity.status(HttpStatus.OK).body(languageService.getConflicts(locale));
	}

}
