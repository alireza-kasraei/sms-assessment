package net.devk.sms.language.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;

@Data
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Language {

	@XmlElement
	private String content;
	@XmlElement
	private String id;
	@XmlElement
	private Date lastModifiedDate;

}
