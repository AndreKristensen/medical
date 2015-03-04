package no.ask.medical.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author Andre
 * Custom PEP Exception 
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class PEPException extends RuntimeException {

	private static final long serialVersionUID = 6614129221801516752L;

	public PEPException(String message) {
		super(message);
	}

	public PEPException(String message, Throwable cause) {
		super(message, cause);
	}
}
