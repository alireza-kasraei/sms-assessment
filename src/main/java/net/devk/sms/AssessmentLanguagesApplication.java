package net.devk.sms;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.devk.sms.language.DefaultLanguageService;
import net.devk.sms.language.LanguageService;
import net.devk.sms.storage.JsonStorageService;
import net.devk.sms.storage.StorageService;
import net.devk.sms.storage.XmlStorageService;

@SpringBootApplication
public class AssessmentLanguagesApplication {

	@Profile("xml")
	@Bean
	public StorageService storageService(Environment environment) {
		Path fileStorageLocation = Paths.get(environment.getProperty("language-path")).toAbsolutePath().normalize();
		return new XmlStorageService(fileStorageLocation.toFile().getAbsolutePath());
	}

	@Profile("json")
	@Bean
	public StorageService jsonStorageService(Environment environment, ObjectMapper objectMapper) {
		Path fileStorageLocation = Paths.get(environment.getProperty("language-path")).toAbsolutePath().normalize();
		return new JsonStorageService(objectMapper, fileStorageLocation.toFile().getAbsolutePath());
	}

	@Bean
	public LanguageService languageService(StorageService storageService) {
		return new DefaultLanguageService(storageService);
	}

	public static void main(String[] args) {
		SpringApplication.run(AssessmentLanguagesApplication.class, args);
	}

}
