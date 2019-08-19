package net.devk.sms.language;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class LanguageAdvisor {

	@ExceptionHandler(IllegalArgumentException.class)
	public void invalidRemoteCustomerData(IllegalArgumentException exception, HttpServletResponse httpServletResponse)
			throws IOException {
		httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
	}

}