package net.devk.sms.language.model;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@XmlRootElement(name = "languageXmlFile")
@XmlAccessorType(XmlAccessType.FIELD)
public class LanguageFile {

	@XmlElementWrapper(name = "language")
	@XmlElement(name = "language")
	private List<Language> language = new LinkedList<>();

	@XmlTransient
	@JsonIgnore
	private String locale;

}
