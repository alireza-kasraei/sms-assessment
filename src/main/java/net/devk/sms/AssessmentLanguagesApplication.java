package net.devk.sms;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import net.devk.sms.language.DefaultLanguageService;
import net.devk.sms.language.LanguageService;
import net.devk.sms.storage.StorageService;
import net.devk.sms.storage.XmlStorageService;

@SpringBootApplication
public class AssessmentLanguagesApplication {

	@Bean
	public StorageService storageService(Environment environment) {
		Path fileStorageLocation = Paths.get(environment.getProperty("language-path")).toAbsolutePath().normalize();
		return new XmlStorageService(fileStorageLocation.toFile().getAbsolutePath());
	}

	@Bean
	public LanguageService languageService(StorageService storageService) {
		return new DefaultLanguageService(storageService);
	}

	public static void main(String[] args) {
		SpringApplication.run(AssessmentLanguagesApplication.class, args);
	}

}
