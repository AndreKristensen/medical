package no.ask.medical.exception;
/**
 * 
 * @author Andre
 * Custom PEP Exception 
 */
public class PEPException extends RuntimeException {

	private static final long serialVersionUID = 6614129221801516752L;

	public PEPException(String message) {
		super(message);
	}

	public PEPException(String message, Throwable cause) {
		super(message, cause);
	}
}
