package no.ask.medical.exception;

public class PEPException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6614129221801516752L;

	public PEPException(String message) {
		super(message);
	}

	public PEPException(String message, Throwable cause) {
		super(message, cause);
	}
}
